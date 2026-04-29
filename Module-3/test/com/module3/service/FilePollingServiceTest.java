package com.module3.service;

import com.module3.io.CsvReaderFactory;
import com.module3.model.Brand;
import com.module3.store.ProductRepository;
import com.module3.validation.ProductValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilePollingServiceTest {
    private final ProductRepository repository = ProductRepository.getInstance();

    @TempDir
    Path tempDir;

    @AfterEach
    void tearDown() {
        repository.clear();
    }

    @Test
    void shouldLoadOnlyNewFilesAndAvoidDuplicateRecords() throws IOException {
        List<String> messages = new ArrayList<>();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        FilePollingService service = new FilePollingService(
                tempDir,
                repository,
                new CsvReaderFactory(new ProductValidator(), messages::add),
                messages::add,
                executor
        );

        Files.writeString(tempDir.resolve("puma-1.csv"), String.join(System.lineSeparator(),
                "ID,Brand,COLOR,SIZE,TYPE",
                "1,PUMA,red,10,SHOE"));
        Files.writeString(tempDir.resolve("Nike-1.csv"), String.join(System.lineSeparator(),
                "ID,Brand,COLOR,SIZE,TYPE,PRICE",
                "1,NIKE,red,10,SHOE,1000"));

        service.scanAllNow();
        assertEquals(2, repository.count());

        Files.writeString(tempDir.resolve("Nike-2.csv"), String.join(System.lineSeparator(),
                "ID,Brand,COLOR,SIZE,TYPE,PRICE",
                "1,NIKE,red,10,SHOE,1000",
                "2,NIKE,blue,9,SHOE,1500"));

        service.scanBrandNow(Brand.NIKE);

        assertEquals(3, repository.count());
        assertTrue(messages.stream().anyMatch(message -> message.contains("Loaded")));
        service.stop();
    }
}

