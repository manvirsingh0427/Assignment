package com.module3.service;

import com.module3.model.Brand;
import com.module3.model.FilterOption;
import com.module3.model.Product;
import com.module3.model.ProductType;
import com.module3.store.ProductRepository;
import com.module3.validation.ProductValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductServiceTest {
    private final ProductRepository repository = ProductRepository.getInstance();
    private final ProductService productService = new ProductService(repository, new ProductValidator());

    @AfterEach
    void tearDown() {
        repository.clear();
    }

    @Test
    void shouldSearchUsingRequestedFilter() {
        repository.addProduct(new Product("1", Brand.PUMA, "red", "10", ProductType.SHOE, null));
        repository.addProduct(new Product("1", Brand.NIKE, "red", "10", ProductType.SHOE, new BigDecimal("1000")));
        repository.addProduct(new Product("2", Brand.NIKE, "blue", "9", ProductType.SHOE, new BigDecimal("900")));

        List<Product> byColorAndSize = productService.search(FilterOption.COLOR_AND_SIZE, "red", "10");
        List<Product> byBrandAndColor = productService.search(FilterOption.BRAND_AND_COLOR, "nike", "red");

        assertEquals(2, byColorAndSize.size());
        assertEquals(1, byBrandAndColor.size());
        assertEquals(Brand.NIKE, byBrandAndColor.get(0).getBrand());
    }

    @Test
    void shouldFormatResultsForDisplay() {
        String formatted = productService.formatResults(List.of(
                new Product("1", Brand.PUMA, "red", "10", ProductType.SHOE, null),
                new Product("1", Brand.NIKE, "red", "10", ProductType.SHOE, new BigDecimal("1000"))
        ));

        assertTrue(formatted.contains("item1 -> id = 1, brand = PUMA"));
        assertTrue(formatted.contains("item2 -> id = 1, brand = NIKE"));
    }

    @Test
    void shouldRejectInvalidBrandInputDuringSearch() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> productService.search(FilterOption.BRAND_AND_SIZE, "adidas", "10"));

        assertEquals("Brand must be either PUMA or NIKE.", exception.getMessage());
    }

    @Test
    void shouldReturnFriendlyMessageForEmptyResults() {
        assertEquals("No items found for the given filter.", productService.formatResults(List.of()));
    }
}

