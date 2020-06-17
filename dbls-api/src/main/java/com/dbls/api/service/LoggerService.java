package com.dbls.api.service;

import java.io.IOException;

import com.dbls.api.dto.CurrentShiftsResponse;
import com.dbls.api.dto.CurrentStateResponse;
import com.dbls.api.dto.CurrentTimingResponse;
import com.dbls.api.dto.LoggerStatusResponse;
import com.dbls.api.dto.NewShiftsRequest;
import com.dbls.api.dto.NewShiftsResponse;
import com.dbls.api.dto.NewTimingRequest;
import com.dbls.api.dto.NewTimingResponse;

public interface LoggerService {

    LoggerStatusResponse loggerStatus();
    CurrentStateResponse currentState();
    NewShiftsResponse newShifts(NewShiftsRequest request) throws IOException;
    CurrentShiftsResponse getCurrentShifts();
    NewTimingResponse newTiming(NewTimingRequest request);
    CurrentTimingResponse getCurrentTiming();

}
