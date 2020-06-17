package com.dbls.impl.service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.TriggerContext;
import org.springframework.stereotype.Component;

import com.dbls.api.service.PersistentDataRepository;

import lombok.extern.slf4j.Slf4j;
import static com.dbls.api.service.PersistentDataRepository.*;

@Slf4j
@Component
public class ScheduledValueGenerator {

    private static final RandomDataGenerator RNG = new RandomDataGenerator();

    @Autowired
    private TaskScheduler scheduler;

    @Autowired
    private PersistentDataRepository dataRepository;

    @Value("${configuration.generator-interval:1000}")
    private long interval;

    private ScheduledFuture<?> task;

    @PostConstruct
    public void startTask() {
        if (null != task) {
            throw new IllegalStateException("Task is already scheduled!");
        }

        String savedInterval = dataRepository.getData(PersistentDataRepository.KEY_INTERVAL);
        if (null != savedInterval) {
            this.interval = Long.parseLong(savedInterval);
        }
        log.info("Starting value generation task. interval={} ms", interval);
        this.task = scheduler.schedule(this::generateValues, this::nextRun);
    }

    private void generateValues() {
        long v1 = RNG.nextLong(0L, 1000L);
        long v2 = RNG.nextLong(0, 1000L);
        dataRepository.putData(KEY_GENERATED_VALUE_ONE, String.valueOf(v1));
        dataRepository.putData(KEY_GENERATED_VALUE_TWO, String.valueOf(v2));
        try {
            dataRepository.saveData();
        } catch (IOException e) {
            log.warn("Could not save generated values.");
        }
        log.info("Generated new values. GENERATED_VALUE_ONE={}, GENERATED_VALUE_TWO={}", v1, v2);
    }

    private Date nextRun(TriggerContext triggerContext) {
        ZonedDateTime now = ZonedDateTime.now();
        return Date.from(now.plus(interval, ChronoField.MILLI_OF_DAY.getBaseUnit()).toInstant());
    }

    public void updateInterval(long interval) {
        log.info("Updating interval. new interval={}", interval);
        this.interval = interval;
    }

    public long getInterval() {
        return this.interval;
    }

}
