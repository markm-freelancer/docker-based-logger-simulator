package com.dbls.impl.service;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dbls.api.dto.CurrentStateResponse;
import com.dbls.api.dto.LoggerStatusResponse;
import com.dbls.api.dto.NewShiftsRequest;
import com.dbls.api.dto.NewShiftsResponse;
import com.dbls.api.dto.NewTimingRequest;
import com.dbls.api.dto.NewTimingResponse;
import com.dbls.api.service.LoggerService;

@Component
public class LoggerServiceImpl implements LoggerService {

    private static final Logger LOG = LoggerFactory.getLogger(LoggerServiceImpl.class);

    @Override
    public LoggerStatusResponse loggerStatus() {
        Map<String, String> macAddress = extractMacAddresses();
        LoggerStatusResponse response = new LoggerStatusResponse();
        response.setMacAddresses(macAddress);
        return response;
    }

    private Map<String, String> extractMacAddresses() {
        Map<String, String> macAddresses = new HashMap<>();
        try {
            Enumeration<NetworkInterface> networkInterface = NetworkInterface.getNetworkInterfaces();
            while (networkInterface.hasMoreElements()) {
                NetworkInterface network = networkInterface.nextElement();
                byte[] macAddressBytes = network.getHardwareAddress();
                if (macAddressBytes != null) {
                    StringBuilder macAddressStr = new StringBuilder();
                    for (int i = 0; i < macAddressBytes.length; i++) {
                        macAddressStr.append(String.format("%02X", macAddressBytes[i]));
                        if(i < macAddressBytes.length - 1) {
                            macAddressStr.append("-");
                        }
                    }
                    macAddresses.put(network.getName(), macAddressStr.toString());
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return macAddresses;
    }

    @Override
    public CurrentStateResponse currentState() {
        CurrentStateResponse response = new CurrentStateResponse();
        response.setPressure(12d);
        response.setTemperature(35.5d);
        response.setVolume(80d);
        response.setWeight(16d);
        response.setStatus("Normal");
        return response;
    }

    @Override
    public NewShiftsResponse newShifts(NewShiftsRequest request) {
        LOG.info("Received new shifts request. request={}", request);
        NewShiftsResponse response = new NewShiftsResponse();
        response.setStatus("OK");
        return response;
    }

    @Override
    public NewTimingResponse newTiming(NewTimingRequest request) {
        LOG.info("Received new timing request. request={}", request);
        NewTimingResponse response = new NewTimingResponse();
        response.setStatus("OK");
        return response;
    }

}
