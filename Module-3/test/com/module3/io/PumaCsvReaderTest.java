package com.module3.io;

import com.module3.model.Product;
import com.module3.validation.ProductValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PumaCsvReaderTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldReadValidProductsAndSkipInvalidRows() throws IOException {
        Path file = tempDir.resolve("puma-1.csv");
        Files.writeString(file, String.join(System.lineSeparator(),
                "ID,Brand,COLOR,SIZE,TYPE",
                "1,PUMA,red,10,SHOE",
                "2,PUMA,blue,M,SHIRT",
                "3,PUMA,green,8,CAP"));

        List<String> errors = new ArrayList<>();
        PumaCsvReader reader = new PumaCsvReader(new ProductValidator(), errors::add);
        List<Product> products = reader.readProducts(file);

        assertEquals(2, products.size());
        assertEquals("red", products.get(0).getColor());
        assertEquals(1, errors.size());
    }

    @Test
    void shouldSupportOnlyPumaFiles() {
        PumaCsvReader reader = new PumaCsvReader(new ProductValidator(), message -> { });

        assertTrue(reader.supports(Path.of("puma-2.csv")));
        assertFalse(reader.supports(Path.of("nike-2.csv")));
    }
}

