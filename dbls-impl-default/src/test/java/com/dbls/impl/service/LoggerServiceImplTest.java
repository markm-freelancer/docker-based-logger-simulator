package com.dbls.impl.service;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import com.dbls.api.dto.CurrentStateResponse;
import com.dbls.api.dto.CurrentStateResponse.CurrentState;
import com.dbls.api.dto.LoggerStatusResponse;
import com.dbls.api.dto.NewShiftsRequest;
import com.dbls.api.dto.NewShiftsResponse;
import com.dbls.api.service.LoggerService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LoggerServiceImplTest {

    @Autowired
    private LoggerService loggerService;

    @Autowired
    private Environment env;

    @Test
    public void whenLoggerStatus_thenSomeMacAddressesReturned() {
        LoggerStatusResponse response = loggerService.loggerStatus();
        String uuid = env.getRequiredProperty("configuration.DEVICE_UUID");
        String status = env.getRequiredProperty("configuration.DEVICE_STATUS");
        assertEquals(response.getId(), uuid);
        assertEquals(response.getStatus(), status);
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

        assertEquals("SIMULATION_LOGGER", currentState.getDevice().getName());
        assertEquals("75-10-0B-0E-C8-0E", currentState.getDevice().getMAC());
    }

    @Test
    public void whenNewShifts_thenOkReturned() throws IOException {
        NewShiftsRequest request = new NewShiftsRequest("03:00", "05:00", "11:00", "14:00", "23:00", "02:00");
        NewShiftsResponse response =  loggerService.newShifts(request);
        assertEquals(response.getStatus(), "OK");
    }

//    @Test
//    public void whenNewTiming_thenOkReturned() {
//        NewTimingRequest request = new NewTimingRequest();
//        NewTimingResponse response =  loggerService.newTiming(request);
//        assertThat(response.getStatus()).isEqualTo("OK");
//    }

}
