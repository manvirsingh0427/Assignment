package com.module3.io;

import com.module3.model.Brand;
import com.module3.model.Product;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Interface for reading products from a CSV file for a specific brand.
 */
public interface BrandCsvReader {
    /**
     * @return The brand this reader supports
     */
    Brand getBrand();

    /**
     * Checks if this reader supports the given file path.
     * @param filePath Path to the CSV file
     * @return true if supported, false otherwise
     */
    boolean supports(Path filePath);

    /**
     * Reads products from the given file path.
     * @param filePath Path to the CSV file
     * @return List of Product objects
     * @throws IOException if file cannot be read
     */
    List<Product> readProducts(Path filePath) throws IOException;
}
