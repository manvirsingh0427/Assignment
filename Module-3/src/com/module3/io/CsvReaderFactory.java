package com.module3.io;

import com.module3.model.Brand;
import com.module3.validation.ProductValidator;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Factory for creating BrandCsvReader instances for different brands.
 * Provides a reader for each supported brand.
 */
public class CsvReaderFactory {
    private final Map<Brand, BrandCsvReader> readers;

    /**
     * Constructs a CsvReaderFactory with a validator and error logger.
     * @param validator ProductValidator for validating products
     * @param errorLogger Consumer for error messages
     */
    public CsvReaderFactory(ProductValidator validator, Consumer<String> errorLogger) {
        Map<Brand, BrandCsvReader> readerMap = new EnumMap<>(Brand.class);
        readerMap.put(Brand.PUMA, new PumaCsvReader(validator, errorLogger));
        readerMap.put(Brand.NIKE, new NikeCsvReader(validator, errorLogger));
        this.readers = readerMap;
    }

    /**
     * Returns the BrandCsvReader for the given brand.
     * @param brand Brand to get reader for
     * @return BrandCsvReader instance
     * @throws IllegalArgumentException if no reader is configured for the brand
     */
    public BrandCsvReader getReader(Brand brand) {
        BrandCsvReader reader = readers.get(brand);
        if (reader == null) {
            throw new IllegalArgumentException("No CSV reader configured for brand: " + brand);
        }
        return reader;
    }
}
