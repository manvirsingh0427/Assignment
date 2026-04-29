package com.module3.model;

/**
 * Enum representing supported product brands.
 */
public enum Brand {
    PUMA,
    NIKE;

    /**
     * Parses a string value to a Brand enum, case-insensitive.
     * @param value Brand name string
     * @return Brand enum value
     * @throws IllegalArgumentException if value is null, empty, or invalid
     */
    public static Brand fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be empty.");
        }
        return Brand.valueOf(value.trim().toUpperCase());
    }
}
