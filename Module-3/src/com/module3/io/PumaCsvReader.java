package com.module3.io;

import com.module3.model.Brand;
import com.module3.model.Product;
import com.module3.validation.ProductValidator;

import java.nio.file.Path;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * CSV reader for Puma brand products.
 * Parses and validates Puma CSV files.
 */
public class PumaCsvReader extends CsvReader {
    private final ProductValidator validator;

    /**
     * Constructs a PumaCsvReader with a validator and error logger.
     * @param validator ProductValidator for Puma products
     * @param errorLogger Consumer for error messages
     */
    public PumaCsvReader(ProductValidator validator, Consumer<String> errorLogger) {
        super(errorLogger);
        this.validator = validator;
    }

    /**
     * @return Brand.PUMA
     */
    @Override
    public Brand getBrand() {
        return Brand.PUMA;
    }

    /**
     * Checks if the file is a Puma CSV file (filename starts with "puma-" and ends with ".csv").
     * @param filePath Path to the CSV file
     * @return true if supported, false otherwise
     */
    @Override
    public boolean supports(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase(Locale.ROOT);
        return fileName.startsWith("puma-") && fileName.endsWith(".csv");
    }

    /**
     * Parses columns into a validated Puma Product.
     * @param columns Array of column values
     * @return Product object
     */
    @Override
    protected Product parse(String[] columns) {
        return validator.validatePuma(columns);
    }
}
