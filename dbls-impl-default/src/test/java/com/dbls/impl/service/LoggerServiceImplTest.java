package com.dbls.impl.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.dbls.api.dto.CurrentStateResponse;
import com.dbls.api.dto.LoggerStatusResponse;
import com.dbls.api.dto.NewShiftsRequest;
import com.dbls.api.dto.NewShiftsResponse;
import com.dbls.api.dto.NewTimingRequest;
import com.dbls.api.dto.NewTimingResponse;

@RunWith(MockitoJUnitRunner.class)
public class LoggerServiceImplTest {

    @InjectMocks
    private LoggerServiceImpl loggerService;

    @Test
    public void whenLoggerStatus_thenSomeMacAddressesReturned() {
        LoggerStatusResponse response = loggerService.loggerStatus();
        assertThat(response.getMacAddresses().size()).isGreaterThan(0);
    }

    @Test
    public void whenCurrentState_thenReturnCurrentStateDto() {
        CurrentStateResponse response = loggerService.currentState();
        assertThat(response.getStatus()).isEqualTo("Normal");
        assertThat(response.getPressure()).isEqualTo(12d);
    }

    @Test
    public void whenNewShifts_thenOkReturned() {
        NewShiftsRequest request = new NewShiftsRequest();
        NewShiftsResponse response =  loggerService.newShifts(request);
        assertThat(response.getStatus()).isEqualTo("OK");
    }

    @Test
    public void whenNewTiming_thenOkReturned() {
        NewTimingRequest request = new NewTimingRequest();
        NewTimingResponse response =  loggerService.newTiming(request);
        assertThat(response.getStatus()).isEqualTo("OK");
    }

}
