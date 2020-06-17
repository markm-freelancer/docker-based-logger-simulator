package com.dbls.api.resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.dbls.api.dto.LoggerStatusResponse;
import com.dbls.api.dto.NewShiftsRequest;
import com.dbls.api.dto.NewShiftsResponse;
import com.dbls.api.dto.NewTimingRequest;
import com.dbls.api.dto.NewTimingResponse;
import com.dbls.api.service.LoggerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@WebMvcTest(LoggerResource.class)
public class LoggerResourceTest {

    @MockBean
    private LoggerService service;

    @Autowired
    private LoggerResource controller;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void loggerStatus() throws Exception {
        String uuid = UUID.randomUUID().toString();

        LoggerStatusResponse response = new LoggerStatusResponse();
        response.setId(uuid);
        response.setStatus("OK");
        when(service.loggerStatus()).thenReturn(response);

        MockHttpServletRequestBuilder builder = get("/logger_status")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8");

        mockMvc
            .perform(builder)
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uuid))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void whenCurrentState_ThenReturnDto() throws Exception {
    }

    @Test
    public void whenNewShifts_ThenReturnOk() throws Exception {
        NewShiftsRequest request = new NewShiftsRequest("03:00", "05:00", "11:00", "14:00", "23:00", "02:00");
        String body = objectMapper.writeValueAsString(request);

        NewShiftsResponse response = new NewShiftsResponse();
        response.setStatus("OK");
        when(service.newShifts(any(NewShiftsRequest.class))).thenReturn(response);

        MockHttpServletRequestBuilder builder = post("/new_shifts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(body);

        mockMvc
            .perform(builder)
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void whenNewTiming_ThenReturnOk() throws Exception {
        NewTimingRequest request = new NewTimingRequest();
        String body = objectMapper.writeValueAsString(request);

        NewTimingResponse response = new NewTimingResponse();
        response.setStatus("OK");
        when(service.newTiming(any(NewTimingRequest.class))).thenReturn(response);

        MockHttpServletRequestBuilder builder = post("/new_timing")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(body);

        mockMvc
            .perform(builder)
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(status().is2xxSuccessful());
    }

}
