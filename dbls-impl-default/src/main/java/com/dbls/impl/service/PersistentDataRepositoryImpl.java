package com.dbls.impl.service;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.dbls.api.service.PersistentDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PersistentDataRepositoryImpl implements PersistentDataRepository {

    @Autowired
    private Environment env;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${configuration.data-filepath}")
    private String filePath;
    private Map<String, String> data;

    @PostConstruct
    public void loadData() throws FileNotFoundException, JsonMappingException, JsonProcessingException {
        log.info("Loading data from file. absolute path={}", Paths.get(filePath).toAbsolutePath().toString());
        StringBuilder stringBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(line -> stringBuilder.append(line));
        } catch (IOException e) {
            log.warn("File not found.");
        }
        String dataStr = stringBuilder.toString();

        try {
            data = objectMapper.readValue(dataStr, Map.class);
        } catch (Exception e) {
            log.warn("Unable to parse source file. data will be empty.");
            data = new HashMap<>();
        }
    }

    @Override
    public String getData(String key) {
        return data.get(key);
    }

    @Override
    public String getData(String key, String defaultValue) {
        String data = getData(key);
        return null != data ? data : defaultValue;
    }

    @Override
    public void putData(String key, String value) {
        data.put(key, value);
    }

    @Override
    public synchronized void saveData() throws IOException {
        String dataStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(dataStr);
        fileWriter.close();
    }

    @Override
    public String getConfigurationProperty(String key) {
        return env.getRequiredProperty("configuration." + key);
    }

}
