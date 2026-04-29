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

class NikeCsvReaderTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldReadNikeProductsWithPrice() throws IOException {
        Path file = tempDir.resolve("Nike-1.csv");
        Files.writeString(file, String.join(System.lineSeparator(),
                "ID,Brand,COLOR,SIZE,TYPE,PRICE",
                "1,NIKE,red,10,SHOE,1000",
                "2,NIKE,blue,M,SHIRT,1499",
                "3,NIKE,black,9,SHOE,-10"));

        List<String> errors = new ArrayList<>();
        NikeCsvReader reader = new NikeCsvReader(new ProductValidator(), errors::add);
        List<Product> products = reader.readProducts(file);

        assertEquals(2, products.size());
        assertEquals("1000", products.get(0).getPrice().stripTrailingZeros().toPlainString());
        assertEquals(1, errors.size());
    }

    @Test
    void shouldSupportOnlyNikeFiles() {
        NikeCsvReader reader = new NikeCsvReader(new ProductValidator(), message -> { });

        assertTrue(reader.supports(Path.of("Nike-2.csv")));
        assertFalse(reader.supports(Path.of("puma-2.csv")));
    }
}

