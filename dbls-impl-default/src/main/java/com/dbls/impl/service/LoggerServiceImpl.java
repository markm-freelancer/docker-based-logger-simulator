package com.dbls.impl.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbls.api.dto.CurrentShiftsResponse;
import com.dbls.api.dto.CurrentStateResponse;
import com.dbls.api.dto.CurrentStateResponse.Device;
import com.dbls.api.dto.CurrentTimingResponse;
import com.dbls.api.dto.LoggerStatusResponse;
import com.dbls.api.dto.Maplike;
import com.dbls.api.dto.Maplike.Entrylike;
import com.dbls.api.dto.NewShiftsRequest;
import com.dbls.api.dto.NewShiftsResponse;
import com.dbls.api.dto.NewTimingRequest;
import com.dbls.api.dto.NewTimingResponse;
import com.dbls.api.service.LoggerService;
import com.dbls.api.service.PersistentDataRepository;
import com.dbls.impl.exception.IllegalShiftIntervalException;
import com.dbls.impl.exception.IllegalTimingException;
import com.dbls.impl.util.TimeUtil;
import static com.dbls.api.service.PersistentDataRepository.*;

@Component
public class LoggerServiceImpl implements LoggerService {

    private static final Logger LOG = LoggerFactory.getLogger(LoggerServiceImpl.class);

    @Autowired
    private PersistentDataRepository dataRepository;

    @Autowired
    private ScheduledValueGenerator scheduledValueGenerator;

    @Override
    public LoggerStatusResponse loggerStatus() {
        String uuid = dataRepository.getSystemProperty(KEY_DEVICE_ID);
        if (null == uuid) {
            uuid = dataRepository.getConfigurationProperty(KEY_DEVICE_ID);
        }
        String status = dataRepository.getSystemProperty(KEY_DEVICE_STATUS);
        if (null == status) {
            status = dataRepository.getConfigurationProperty(KEY_DEVICE_STATUS);
        }
        return new LoggerStatusResponse(uuid, status);
    }

    @Override
    public CurrentStateResponse currentState() {
        CurrentStateResponse response = new CurrentStateResponse();

        CurrentStateResponse.CurrentState currentState = response.new CurrentState();
        response.setCurrentState(currentState);

        Maplike digitalInput = new Maplike();
        for (int i = 1; i < 16; i++) {
            Maplike.Entrylike entry = digitalInput.new Entrylike("DI" + i, "0");
            digitalInput.add(entry);
        }
        currentState.setDigitalInput(digitalInput);

        Maplike analogInput = new Maplike();
        analogInput.add(analogInput.new Entrylike("AI1", dataRepository.getData(KEY_GENERATED_VALUE_ONE)));
        analogInput.add(analogInput.new Entrylike("AI2", dataRepository.getData(KEY_GENERATED_VALUE_TWO)));
        for (int i = 3; i < 8; i++) {
            Entrylike entry = analogInput.new Entrylike("AI" + i, "0");
            analogInput.add(entry);
        }
        currentState.setAnalogInput(analogInput);

        Maplike temperatureInput = new Maplike();
        for (int i = 1; i < 8; i++) {
            Maplike.Entrylike entry = temperatureInput.new Entrylike("TI" + i, "");
            temperatureInput.add(entry);
        }
        currentState.setTemperatureInput(temperatureInput);

        Device device = response.new Device();
        device.setName(dataRepository.getConfigurationProperty("DEVICE_NAME"));
        device.setMac(dataRepository.getConfigurationProperty("DEVICE_MAC_ADDRESS"));
        currentState.setDevice(device);

        return response;
    }

    @Override
    public NewShiftsResponse newShifts(NewShiftsRequest request) throws IOException {
        LOG.info("Received new shifts request. request={}", request);
        verifyShiftInterval(request.getShiftOneStart(), request.getShiftOneEnd());
        verifyShiftInterval(request.getShiftTwoStart(), request.getShiftTwoEnd());
        verifyShiftInterval(request.getShiftThreeStart(), request.getShiftThreeEnd());

        dataRepository.putData("shiftOneStart", request.getShiftOneStart());
        dataRepository.putData("shiftOneEnd", request.getShiftOneEnd());
        dataRepository.putData("shiftTwoStart", request.getShiftTwoStart());
        dataRepository.putData("shiftTwoEnd", request.getShiftTwoEnd());
        dataRepository.putData("shiftThreeStart", request.getShiftThreeStart());
        dataRepository.putData("shiftThreeEnd", request.getShiftThreeEnd());
        dataRepository.saveData();

        NewShiftsResponse response = new NewShiftsResponse();
        response.setStatus("OK");
        return response;
    }

    private void verifyShiftInterval(String shiftStart, String shiftEnd) {
        LocalDateTime start = TimeUtil.parseDateTime(shiftStart);
        LocalDateTime end = TimeUtil.parseDateTime(shiftEnd);
        if (end.isBefore(start)) {
            end = end.plusDays(1L);
        }
        if (start.plusHours(12).isBefore(end)) {
            throw new IllegalShiftIntervalException("Shift interval can't be longer than 12 hours: " + TimeUtil.readableTime(start) + " - " + TimeUtil.readableTime(end));
        }
    }

    @Override
    public CurrentShiftsResponse getCurrentShifts() {
        CurrentShiftsResponse response = new CurrentShiftsResponse();
        response.setShiftOneStart(dataRepository.getData(KEY_SHIFT_ONE_START));
        response.setShiftOneEnd(dataRepository.getData(KEY_SHIFT_ONE_END));
        response.setShiftTwoStart(dataRepository.getData(KEY_SHIFT_TWO_START));
        response.setShiftTwoEnd(dataRepository.getData(KEY_SHIFT_TWO_END));
        response.setShiftThreeStart(dataRepository.getData(KEY_SHIFT_THREE_START));
        response.setShiftThreeEnd(dataRepository.getData(KEY_SHIFT_THREE_END));
        return response;
    }

    @Override
    public NewTimingResponse newTiming(NewTimingRequest request) {
        LOG.info("Received new timing request. request={}", request);

        long interval = request.getNewTiming();
        if (interval < 1000 || interval > 10000) {
            throw new IllegalTimingException("Timing must be in the range 1000 - 10000 ms");
        }
        scheduledValueGenerator.updateInterval(interval);
        dataRepository.putData("interval", String.valueOf(request.getNewTiming()));
        NewTimingResponse response = new NewTimingResponse();
        response.setStatus("OK");
        return response;
    }

    @Override
    public CurrentTimingResponse getCurrentTiming() {
        CurrentTimingResponse response = new CurrentTimingResponse();
        response.setInterval(scheduledValueGenerator.getInterval());
        return response;
    }

}
