package com.dbls.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dbls.api.dto.CurrentStateResponse;
import com.dbls.api.dto.LoggerStatusResponse;
import com.dbls.api.dto.NewShiftsRequest;
import com.dbls.api.dto.NewShiftsResponse;
import com.dbls.api.dto.NewTimingRequest;
import com.dbls.api.dto.NewTimingResponse;
import com.dbls.api.service.LoggerService;

@RestController
public class LoggerResource {

    @Autowired
    private LoggerService service;

    @GetMapping("/logger_status")
    public ResponseEntity<LoggerStatusResponse> loggerStatus() {
        return ResponseEntity.ok(service.loggerStatus());
    }

    @GetMapping("/current_state")
    public ResponseEntity<CurrentStateResponse> currentState() {
        return ResponseEntity.ok(service.currentState());
    }

    @PostMapping("/new_shifts")
    public ResponseEntity<NewShiftsResponse> newShifts(@RequestBody NewShiftsRequest request) {
        return ResponseEntity.ok(service.newShifts(request));
    }

    @PostMapping("/new_timing")
    public ResponseEntity<NewTimingResponse> newTiming(@RequestBody NewTimingRequest request) {
        return ResponseEntity.ok(service.newTiming(request));
    }

}
