package com.module3.io;

import com.module3.model.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Abstract base class for reading CSV files for a specific brand.
 * Implements common logic for reading lines and parsing products.
 * Subclasses must implement the parse method for brand-specific parsing.
 */
public abstract class CsvReader implements BrandCsvReader {
    private final Consumer<String> errorLogger;

    /**
     * Constructs an AbstractCsvReader with an error logger.
     * @param errorLogger Consumer for error messages
     */
    protected CsvReader(Consumer<String> errorLogger) {
        this.errorLogger = errorLogger;
    }

    /**
     * Reads products from a CSV file at the given path.
     * Skips header and invalid lines, logs errors.
     * @param filePath Path to the CSV file
     * @return List of parsed Product objects
     * @throws IOException if file cannot be read
     */
    @Override
    public List<Product> readProducts(Path filePath) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        List<Product> products = new ArrayList<>();

        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index);
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            if (isHeader(line)) {
                continue;
            }

            String[] columns = splitCsvLine(line);
            try {
                products.add(parse(columns));
            } catch (IllegalArgumentException ex) {
                errorLogger.accept(String.format("Skipping invalid line %d in %s: %s", index + 1, filePath.getFileName(), ex.getMessage()));
            }
        }

        return products;
    }

    /**
     * Checks if a line is a CSV header.
     * @param line Line to check
     * @return true if header, false otherwise
     */
    private boolean isHeader(String line) {
        return line.trim().toUpperCase().startsWith("ID,");
    }

    /**
     * Splits a CSV line into columns, trimming whitespace.
     * @param line CSV line
     * @return Array of column values
     */
    protected String[] splitCsvLine(String line) {
        return line.split("\\s*,\\s*");
    }

    /**
     * Parses columns into a Product. Must be implemented by subclasses.
     * @param columns Array of column values
     * @return Product object
     */
    protected abstract Product parse(String[] columns);
}
