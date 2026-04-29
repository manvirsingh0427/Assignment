package com.module3.validation;

import com.module3.model.Brand;
import com.module3.model.Product;
import com.module3.model.ProductType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductValidatorTest {
    private final ProductValidator validator = new ProductValidator();

    @Test
    void shouldValidatePumaRecord() {
        Product product = validator.validatePuma(new String[]{"1", "PUMA", "Red", "10", "shoe"});

        assertEquals("1", product.getId());
        assertEquals(Brand.PUMA, product.getBrand());
        assertEquals("red", product.getColor());
        assertEquals("10", product.getSize());
        assertEquals(ProductType.SHOE, product.getType());
    }

    @Test
    void shouldValidateNikeRecordWithPrice() {
        Product product = validator.validateNike(new String[]{"1", "NIKE", "Blue", "M", "shirt", "1999"});

        assertEquals(Brand.NIKE, product.getBrand());
        assertEquals("blue", product.getColor());
        assertEquals("M", product.getSize());
        assertNotNull(product.getPrice());
    }

    @Test
    void shouldRejectInvalidType() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validatePuma(new String[]{"1", "PUMA", "Red", "10", "cap"}));

        assertEquals("Type must be either SHIRT or SHOE.", exception.getMessage());
    }

    @Test
    void shouldRejectInvalidBrand() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validator.parseBrand("ADIDAS"));

        assertEquals("Brand must be either PUMA or NIKE.", exception.getMessage());
    }

    @Test
    void shouldRejectNonPositiveNikePrice() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validateNike(new String[]{"1", "NIKE", "Blue", "M", "shirt", "0"}));

        assertEquals("PRICE must be greater than 0.", exception.getMessage());
    }

    @Test
    void shouldNormalizeFilterValues() {
        assertEquals("red", validator.normalizeFilterValue("  ReD  ", "COLOR"));
        assertEquals("XL", validator.normalizeSize(" xl "));
    }
}

