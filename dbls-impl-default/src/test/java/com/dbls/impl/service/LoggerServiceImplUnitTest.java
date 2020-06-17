package com.dbls.impl.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.dbls.api.dto.LoggerStatusResponse;
import com.dbls.api.service.PersistentDataRepository;

@RunWith(MockitoJUnitRunner.class)
public class LoggerServiceImplUnitTest {

    @Mock
    private PersistentDataRepository repository;

    @InjectMocks
    private LoggerServiceImpl loggerService;

    @Test
    public void whenSystemParameterPresent_thenOverrideConfigurationParameter() {
        when(repository.getSystemProperty("DEVICE_UUID")).thenReturn("123");
        when(repository.getSystemProperty("DEVICE_STATUS")).thenReturn("OK");

        LoggerStatusResponse response = loggerService.loggerStatus();

        assertEquals("123", response.getId());
        assertEquals("OK", response.getStatus());
    }

}
