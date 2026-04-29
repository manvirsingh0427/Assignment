package com.module3.model;

/**
 * Enum representing product types (e.g., shirt, shoe).
 */
public enum ProductType {
    SHIRT,
    SHOE;

    /**
     * Parses a string value to a ProductType enum, case-insensitive.
     * @param value Product type string
     * @return ProductType enum value
     * @throws IllegalArgumentException if value is null, empty, or invalid
     */
    public static ProductType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot be empty.");
        }
        return ProductType.valueOf(value.trim().toUpperCase());
    }
}
