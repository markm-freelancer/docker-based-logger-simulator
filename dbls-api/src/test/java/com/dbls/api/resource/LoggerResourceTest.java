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

import com.dbls.api.dto.CurrentShiftsResponse;
import com.dbls.api.dto.CurrentStateResponse;
import com.dbls.api.dto.CurrentStateResponse.CurrentState;
import com.dbls.api.dto.CurrentStateResponse.Device;
import com.dbls.api.dto.CurrentTimingResponse;
import com.dbls.api.dto.LoggerStatusResponse;
import com.dbls.api.dto.Maplike;
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
        CurrentStateResponse response = new CurrentStateResponse();
        CurrentState currentState = response.new CurrentState();
        response.setCurrentState(currentState);

        Maplike digitalInput = new Maplike();
        digitalInput.put("DI1", "12345");
        currentState.setDigitalInput(digitalInput);

        Maplike analogInput = new Maplike();
        analogInput.put("AI1", "54321");
        currentState.setAnalogInput(analogInput);

        Maplike temperatureInput = new Maplike();
        temperatureInput.put("TI1", "");
        currentState.setTemperatureInput(temperatureInput);

        Device device = response.new Device();
        device.setMac("01-01-01-01-01-01");
        device.setName("abcde");
        currentState.setDevice(device);

        when(service.currentState()).thenReturn(response);

        MockHttpServletRequestBuilder builder = get("/current_state")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8");

        mockMvc
            .perform(builder)
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.CurrentState.DigitalInput[0].Name").value("DI1"))
            .andExpect(jsonPath("$.CurrentState.DigitalInput[0].Value").value("12345"))
            .andExpect(jsonPath("$.CurrentState.AnalogInput[0].Name").value("AI1"))
            .andExpect(jsonPath("$.CurrentState.AnalogInput[0].Value").value("54321"))
            .andExpect(jsonPath("$.CurrentState.TemperatureInput[0].Name").value("TI1"))
            .andExpect(jsonPath("$.CurrentState.TemperatureInput[0].Value").value(""))
            .andExpect(jsonPath("$.CurrentState.Device.MAC").value("01-01-01-01-01-01"))
            .andExpect(jsonPath("$.CurrentState.Device.Name").value("abcde"))
            .andExpect(status().is2xxSuccessful());
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
    public void whenCurrentShifts_thenReturnData() throws Exception {
        CurrentShiftsResponse response = new CurrentShiftsResponse();
        response.setShiftOneStart("01:00");
        response.setShiftOneEnd("02:00");
        response.setShiftTwoStart("03:00");
        response.setShiftTwoEnd("04:00");
        response.setShiftThreeStart("05:00");
        response.setShiftThreeEnd("06:00");

        when(service.getCurrentShifts()).thenReturn(response);

        MockHttpServletRequestBuilder builder = get("/current_shifts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8");

        mockMvc
            .perform(builder)
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.shiftOneStart").value("01:00"))
            .andExpect(jsonPath("$.shiftOneEnd").value("02:00"))
            .andExpect(jsonPath("$.shiftTwoStart").value("03:00"))
            .andExpect(jsonPath("$.shiftTwoEnd").value("04:00"))
            .andExpect(jsonPath("$.shiftThreeStart").value("05:00"))
            .andExpect(jsonPath("$.shiftThreeEnd").value("06:00"))
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

    @Test
    public void whenCurrentTiming_thenReturnData() throws Exception {
        CurrentTimingResponse response = new CurrentTimingResponse();
        response.setInterval(3400L);

        when(service.getCurrentTiming()).thenReturn(response);

        MockHttpServletRequestBuilder builder = get("/current_timing")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8");

        mockMvc
            .perform(builder)
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.interval").value("3400"))
            .andExpect(status().is2xxSuccessful());
    }

}
