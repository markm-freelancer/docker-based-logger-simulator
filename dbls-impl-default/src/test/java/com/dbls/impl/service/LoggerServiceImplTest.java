package com.dbls.impl.service;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import com.dbls.api.dto.CurrentShiftsResponse;
import com.dbls.api.dto.CurrentStateResponse;
import com.dbls.api.dto.CurrentStateResponse.CurrentState;
import com.dbls.api.dto.CurrentTimingResponse;
import com.dbls.api.dto.LoggerStatusResponse;
import com.dbls.api.dto.NewShiftsRequest;
import com.dbls.api.dto.NewShiftsResponse;
import com.dbls.api.dto.NewTimingRequest;
import com.dbls.api.dto.NewTimingResponse;
import com.dbls.api.service.LoggerService;
import com.dbls.impl.exception.IllegalShiftIntervalException;
import com.dbls.impl.exception.IllegalTimingException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LoggerServiceImplTest {

    @Autowired
    private LoggerService loggerService;

    @Autowired
    private Environment env;

    @Before
    public void restoreOriginalData() throws IOException {
        Files.delete(new File("src/test/resources/data.json").toPath());
        Files.copy(new File("src/test/resources/source-data.json").toPath(), new File("src/test/resources/data.json").toPath());
    }

    @Test
    public void whenLoggerStatus_thenSomeMacAddressesReturned() {
        LoggerStatusResponse response = loggerService.loggerStatus();
        String uuid = env.getRequiredProperty("configuration.DEVICE_UUID");
        String status = env.getRequiredProperty("configuration.DEVICE_STATUS");
        assertEquals(uuid, response.getId());
        assertEquals(status, response.getStatus());
    }

    @Test
    public void whenCurrentState_thenReturnCurrentStateDto() {
        CurrentStateResponse response = loggerService.currentState();
        CurrentState currentState = response.getCurrentState();
        System.out.println(response);

        assertEquals(currentState.getDigitalInput().get("DI1").getValue(), "0");

        assertNull(currentState.getAnalogInput().get("AI0"));
        assertEquals(currentState.getAnalogInput().get("AI1").getValue(), "123");
        assertEquals(currentState.getAnalogInput().get("AI2").getValue(), "456");
        assertEquals(currentState.getAnalogInput().get("AI3").getValue(), "0");

        assertNull(currentState.getAnalogInput().get("TI0"));
        assertEquals(currentState.getTemperatureInput().get("TI1").getValue(), "");

        assertEquals("SIMULATION_LOGGER_TEST", currentState.getDevice().getName());
        assertEquals("75-10-0B-0E-C8-0E", currentState.getDevice().getMac());
    }

    @Test
    public void whenNewShifts_thenOkReturned() throws IOException {
        NewShiftsRequest request = new NewShiftsRequest("03:00", "05:00", "11:00", "14:00", "23:00", "02:00");
        NewShiftsResponse response =  loggerService.newShifts(request);
        assertEquals("OK", response.getStatus());
    }

    @Test(expected = IllegalShiftIntervalException.class)
    public void whenNewShiftsInvalid_thenError() throws IOException {
        NewShiftsRequest request = new NewShiftsRequest("03:00", "16:00", "11:00", "14:00", "23:00", "02:00");
        loggerService.newShifts(request);
    }

    @Test
    public void whenCurrentShifts_thenReturnValue() throws IOException {
        NewShiftsRequest request = new NewShiftsRequest("06:00", "07:00", "11:00", "14:00", "23:00", "02:00");
        loggerService.newShifts(request);

        CurrentShiftsResponse response = loggerService.getCurrentShifts();
        assertEquals("06:00", response.getShiftOneStart());
        assertEquals("07:00", response.getShiftOneEnd());
        assertEquals("11:00", response.getShiftTwoStart());
        assertEquals("14:00", response.getShiftTwoEnd());
        assertEquals("23:00", response.getShiftThreeStart());
        assertEquals("02:00", response.getShiftThreeEnd());
    }

    @Test
    public void whenNewTiming_thenOkReturned() {
        NewTimingRequest request = new NewTimingRequest();
        request.setNewTiming(5000L);
        NewTimingResponse response =  loggerService.newTiming(request);
        assertEquals("OK", response.getStatus());
    }

    @Test(expected = IllegalTimingException.class)
    public void whenNewTimingInvalid_thenError() {
        NewTimingRequest request = new NewTimingRequest();
        request.setNewTiming(15000L);
        loggerService.newTiming(request);
    }

    @Test
    public void whenGetCurrentTiming_thenReturnValue() {
        CurrentTimingResponse response = loggerService.getCurrentTiming();
        assertNotEquals(0L, response.getInterval());
    }
}
