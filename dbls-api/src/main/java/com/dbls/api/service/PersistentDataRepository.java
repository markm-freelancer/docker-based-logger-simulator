package com.dbls.api.service;

import java.io.IOException;

public interface PersistentDataRepository {

    String KEY_INTERVAL = "interval";
    String KEY_GENERATED_VALUE_ONE = "GENERATED_VALUE_ONE";
    String KEY_GENERATED_VALUE_TWO = "GENERATED_VALUE_TWO";
    String KEY_DEVICE_ID = "DEVICE_UUID";
    String KEY_DEVICE_STATUS = "DEVICE_STATUS";
    String KEY_SHIFT_ONE_START = "shiftOneStart";
    String KEY_SHIFT_ONE_END = "shiftOneEnd";
    String KEY_SHIFT_TWO_START = "shiftTwoStart";
    String KEY_SHIFT_TWO_END = "shiftTwoEnd";
    String KEY_SHIFT_THREE_START = "shiftThreeStart";
    String KEY_SHIFT_THREE_END = "shiftThreeEnd";

    String getData(String key);
    String getData(String key, String defaultValue);
    String getConfigurationProperty(String key);
    void putData(String key, String value);
    void saveData() throws IOException;
    String getSystemProperty(String key);

}
