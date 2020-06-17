package com.dbls.api.dto;

import lombok.Data;

@Data
public class CurrentStateResponse {
    private CurrentStateResponse.CurrentState CurrentState;

    @Data
    public class CurrentState {
        private Maplike DigitalInput;
        private Maplike AnalogInput;
        private Maplike TemperatureInput;
        private CurrentStateResponse.Device Device;
    }

    @Data
    public class Device {
        private String Name;
        private String MAC;
    }
}
