package com.dbls.api.dto;

import java.util.Map;

public class LoggerStatusResponse {

    private Map<String, String> macAddresses;

    public Map<String, String> getMacAddresses() {
        return macAddresses;
    }

    public void setMacAddresses(Map<String, String> macAddresses) {
        this.macAddresses = macAddresses;
    }

}
