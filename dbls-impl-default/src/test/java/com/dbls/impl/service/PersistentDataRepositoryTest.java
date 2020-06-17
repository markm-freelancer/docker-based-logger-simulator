package com.dbls.impl.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dbls.api.service.PersistentDataRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PersistentDataRepositoryTest {

    @Autowired
    private PersistentDataRepository repository;

    @Before
    public void restoreOriginalData() throws IOException {
        Files.delete(new File("src/test/resources/data.json").toPath());
        Files.copy(new File("src/test/resources/source-data.json").toPath(), new File("src/test/resources/data.json").toPath());
    }

    @Test
    public void whenGetExistingValue_thenReturnValue() {
        String gv1 = repository.getData(PersistentDataRepository.KEY_GENERATED_VALUE_ONE);
        String gv2 = repository.getData(PersistentDataRepository.KEY_GENERATED_VALUE_TWO);
        assertEquals("123", gv1);
        assertEquals("456", gv2);

        gv1 = repository.getData(PersistentDataRepository.KEY_GENERATED_VALUE_ONE, "789");
        gv2 = repository.getData(PersistentDataRepository.KEY_GENERATED_VALUE_TWO, "789");
        assertEquals("123", gv1);
        assertEquals("456", gv2);
    }

    @Test
    public void whenGetNonexistentData_thenReturnNull() {
        String gv3 = repository.getData("KEY_GENERATED_VALUE_THREE");
        assertNull(gv3);
    }

    @Test
    public void whenGetNonexistentDataWithDefault_thenReturnDefault() {
        String gv3 = repository.getData("KEY_GENERATED_VALUE_THREE", "789");
        assertEquals("789", gv3);
    }

    @Test
    public void whenGetConfigurationProperty_thenReturnIfExists() {
        String filepath = repository.getConfigurationProperty("data-filepath");
        assertEquals("src/test/resources/data.json", filepath);
    }

    @Test(expected = IllegalStateException.class)
    public void whenGetNonexistentConfigurationProperty_thenStartupException() {
        repository.getConfigurationProperty("bad-data-filepath");
    }

    @Test
    public void whenPutData_thenStoredInMemory() {
        repository.putData("newKey", "newValue");
        assertEquals("newValue", repository.getData("newKey"));
    }

    @Test
    public void whenSaveData_thenPersistToFile() throws IOException {
        repository.putData("newKey", "newValue");
        repository.saveData();
        ((PersistentDataRepositoryImpl) repository).loadData();
        assertEquals("newValue", repository.getData("newKey"));
    }

}
