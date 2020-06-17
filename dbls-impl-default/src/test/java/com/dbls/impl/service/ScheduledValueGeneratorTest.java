package com.dbls.impl.service;

import static com.dbls.api.service.PersistentDataRepository.KEY_GENERATED_VALUE_ONE;
import static com.dbls.api.service.PersistentDataRepository.KEY_GENERATED_VALUE_TWO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dbls.api.service.PersistentDataRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ScheduledValueGeneratorTest {

    @Autowired
    private ScheduledValueGenerator generator;

    @Autowired
    private PersistentDataRepository repository;

    @Test(expected = IllegalStateException.class)
    public void whenDoubleTaskStart_thenException() {
        generator.startTask();
    }

    @Test
    public void whenGenerateValues_thenPersisted() throws InterruptedException {
        assertNotNull(repository.getData(KEY_GENERATED_VALUE_ONE));
        assertNotNull(repository.getData(KEY_GENERATED_VALUE_TWO));
    }

    @Test
    public void whenUpdateInterval_thenUpdated() {
        long newInterval = 5000L;
        generator.updateInterval(newInterval);
        assertEquals(newInterval, generator.getInterval());
    }
}
