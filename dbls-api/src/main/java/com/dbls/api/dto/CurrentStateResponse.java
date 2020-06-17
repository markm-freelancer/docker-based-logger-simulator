package com.dbls.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CurrentStateResponse {
    @JsonProperty("CurrentState")
    private CurrentStateResponse.CurrentState currentState;

    @Data
    public class CurrentState {
        @JsonProperty("DigitalInput")
        private Maplike digitalInput;
        @JsonProperty("AnalogInput")
        private Maplike analogInput;
        @JsonProperty("TemperatureInput")
        private Maplike temperatureInput;
        @JsonProperty("Device")
        private CurrentStateResponse.Device device;
    }

    @Data
    public class Device {
        @JsonProperty("Name")
        private String name;
        @JsonProperty("MAC")
        private String mac;
    }
}
