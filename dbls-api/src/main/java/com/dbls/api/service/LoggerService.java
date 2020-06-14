package com.dbls.api.service;

import com.dbls.api.dto.CurrentStateResponse;
import com.dbls.api.dto.LoggerStatusResponse;
import com.dbls.api.dto.NewShiftsRequest;
import com.dbls.api.dto.NewShiftsResponse;
import com.dbls.api.dto.NewTimingRequest;
import com.dbls.api.dto.NewTimingResponse;

public interface LoggerService {

    LoggerStatusResponse loggerStatus();
    CurrentStateResponse currentState();
    NewShiftsResponse newShifts(NewShiftsRequest request);
    NewTimingResponse newTiming(NewTimingRequest request);

}
