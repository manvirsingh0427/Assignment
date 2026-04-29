package com.module3.io;

import com.module3.model.Brand;
import com.module3.model.Product;
import com.module3.validation.ProductValidator;

import java.nio.file.Path;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * CSV reader for Nike brand products.
 * Parses and validates Nike CSV files.
 */
public class NikeCsvReader extends CsvReader {
    private final ProductValidator validator;

    /**
     * Constructs a NikeCsvReader with a validator and error logger.
     * @param validator ProductValidator for Nike products
     * @param errorLogger Consumer for error messages
     */
    public NikeCsvReader(ProductValidator validator, Consumer<String> errorLogger) {
        super(errorLogger);
        this.validator = validator;
    }

    /**
     * @return Brand.NIKE
     */
    @Override
    public Brand getBrand() {
        return Brand.NIKE;
    }

    /**
     * Checks if the file is a Nike CSV file (filename starts with "nike-" and ends with ".csv").
     * @param filePath Path to the CSV file
     * @return true if supported, false otherwise
     */
    @Override
    public boolean supports(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase(Locale.ROOT);
        return fileName.startsWith("nike-") && fileName.endsWith(".csv");
    }

    /**
     * Parses columns into a validated Nike Product.
     * @param columns Array of column values
     * @return Product object
     */
    @Override
    protected Product parse(String[] columns) {
        return validator.validateNike(columns);
    }
}
