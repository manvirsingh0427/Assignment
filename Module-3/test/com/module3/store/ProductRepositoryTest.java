package com.module3.store;

import com.module3.model.Brand;
import com.module3.model.Product;
import com.module3.model.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link ProductRepository}.
 * Tests adding, searching, and sorting products in the repository.
 */
class ProductRepositoryTest {
    // Use the singleton repository instance for all tests
    private final ProductRepository repository = ProductRepository.getInstance();

    /**
     * Clear the repository after each test to ensure isolation.
     */
    @AfterEach
    void tearDown() {
        repository.clear();
    }

    /**
     * Test that a product can only be added once for the same brand and id.
     */
    @Test
    void shouldAddProductOnlyOnceForSameBrandAndId() {
        Product puma = new Product("1", Brand.PUMA, "red", "10", ProductType.SHOE, null);

        assertTrue(repository.addProduct(puma));
        assertFalse(repository.addProduct(puma));
        assertEquals(1, repository.count());
    }

    /**
     * Test searching products by different indexes (color, size, brand).
     */
    @Test
    void shouldSearchByDifferentIndexes() {
        Product puma = new Product("1", Brand.PUMA, "red", "10", ProductType.SHOE, null);
        Product nike = new Product("1", Brand.NIKE, "red", "10", ProductType.SHOE, new BigDecimal("1000"));
        Product nikeBlue = new Product("2", Brand.NIKE, "blue", "9", ProductType.SHOE, new BigDecimal("900"));

        repository.addProducts(Arrays.asList(puma, nike, nikeBlue));

        assertEquals(2, repository.findByColor("red").size());
        assertEquals(2, repository.findBySize("10").size());
        assertEquals(2, repository.findByColorAndSize("red", "10").size());
        assertEquals(1, repository.findByBrandAndColor(Brand.NIKE, "red").size());
        assertEquals(1, repository.findByBrandAndSize(Brand.NIKE, "9").size());
    }

    /**
     * Test that all products are returned sorted by brand and id.
     */
    @Test
    void shouldReturnAllProductsSorted() {
        Product puma = new Product("2", Brand.PUMA, "red", "10", ProductType.SHOE, null);
        Product nike = new Product("1", Brand.NIKE, "red", "10", ProductType.SHOE, new BigDecimal("1000"));
        repository.addProducts(List.of(puma, nike));

        List<Product> allProducts = repository.findAll();

        assertEquals(2, allProducts.size());
        assertEquals(Brand.NIKE, allProducts.get(0).getBrand());
        assertEquals(Brand.PUMA, allProducts.get(1).getBrand());
    }
}
