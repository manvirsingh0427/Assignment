package com.module3.validation;

import com.module3.model.Brand;
import com.module3.model.Product;
import com.module3.model.ProductType;

import java.math.BigDecimal;

public class ProductValidator {

    public Product validatePuma(String[] columns) {
        if (columns.length != 5) {
            throw new IllegalArgumentException("PUMA record must contain exactly 5 columns: ID, Brand, COLOR, SIZE, TYPE.");
        }

        String id = normalizeRequired(columns[0], "ID");
        Brand brand = parseBrand(columns[1]);
        if (brand != Brand.PUMA) {
            throw new IllegalArgumentException("PUMA file can contain only PUMA brand records.");
        }
        String color = normalizeText(columns[2], "COLOR");
        String size = normalizeSize(columns[3]);
        ProductType type = parseType(columns[4]);

        return new Product(id, brand, color, size, type, null);
    }

    public Product validateNike(String[] columns) {
        if (columns.length != 6) {
            throw new IllegalArgumentException("NIKE record must contain exactly 6 columns: ID, Brand, COLOR, SIZE, TYPE, PRICE.");
        }

        String id = normalizeRequired(columns[0], "ID");
        Brand brand = parseBrand(columns[1]);
        if (brand != Brand.NIKE) {
            throw new IllegalArgumentException("NIKE file can contain only NIKE brand records.");
        }
        String color = normalizeText(columns[2], "COLOR");
        String size = normalizeSize(columns[3]);
        ProductType type = parseType(columns[4]);
        BigDecimal price = parsePrice(columns[5]);

        return new Product(id, brand, color, size, type, price);
    }

    public Brand parseBrand(String value) {
        try {
            return Brand.fromValue(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Brand must be either PUMA or NIKE.");
        }
    }

    public ProductType parseType(String value) {
        try {
            return ProductType.fromValue(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Type must be either SHIRT or SHOE.");
        }
    }

    public String normalizeFilterValue(String value, String fieldName) {
        return normalizeText(value, fieldName);
    }

    public String normalizeSize(String value) {
        return normalizeRequired(value, "SIZE").toUpperCase();
    }

    private BigDecimal parsePrice(String value) {
        String normalizedValue = normalizeRequired(value, "PRICE");
        try {
            BigDecimal price = new BigDecimal(normalizedValue);
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("PRICE must be greater than 0.");
            }
            return price;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("PRICE must be a valid number.");
        }
    }

    private String normalizeText(String value, String fieldName) {
        return normalizeRequired(value, fieldName).toLowerCase();
    }

    private String normalizeRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }
}

