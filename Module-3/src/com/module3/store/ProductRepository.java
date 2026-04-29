package com.module3.store;

import com.module3.model.Brand;
import com.module3.model.Product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Repository for storing and retrieving Product objects.
 * Implements various indexes for efficient search by color, size, brand, etc.
 * Singleton pattern is used to ensure a single instance.
 */
public final class ProductRepository {
    private static final ProductRepository INSTANCE = new ProductRepository();

    // Main storage for products, keyed by unique product key
    private final ConcurrentMap<String, Product> productsByKey = new ConcurrentHashMap<>();
    // Indexes for fast lookup
    private final ConcurrentMap<String, Set<String>> colorIndex = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Set<String>> sizeIndex = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Set<String>> colorSizeIndex = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Set<String>> brandColorIndex = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Set<String>> brandSizeIndex = new ConcurrentHashMap<>();

    /**
     * Private constructor for singleton pattern.
     */
    private ProductRepository() {
    }

    /**
     * Returns the singleton instance of ProductRepository.
     * @return ProductRepository instance
     */
    public static ProductRepository getInstance() {
        return INSTANCE;
    }

    /**
     * Adds a product to the repository if it does not already exist (by brand and id).
     * Also updates all relevant indexes.
     * @param product Product to add
     * @return true if product was added, false if already present
     */
    public boolean addProduct(Product product) {
        Product existing = productsByKey.putIfAbsent(product.uniqueKey(), product);
        if (existing != null) {
            return false;
        }

        // Update indexes for fast search
        addIndexValue(colorIndex, product.getColor(), product.uniqueKey());
        addIndexValue(sizeIndex, product.getSize(), product.uniqueKey());
        addIndexValue(colorSizeIndex, composite(product.getColor(), product.getSize()), product.uniqueKey());
        addIndexValue(brandColorIndex, composite(product.getBrand().name(), product.getColor()), product.uniqueKey());
        addIndexValue(brandSizeIndex, composite(product.getBrand().name(), product.getSize()), product.uniqueKey());
        return true;
    }

    /**
     * Adds multiple products to the repository.
     * @param products Collection of products
     * @return number of products actually added
     */
    public int addProducts(Collection<Product> products) {
        return (int) products.stream()
                .filter(this::addProduct)
                .count();
    }

    /**
     * Finds products by color.
     * @param color Color to search
     * @return List of matching products
     */
    public List<Product> findByColor(String color) {
        return productsFromIndex(colorIndex, color);
    }

    /**
     * Finds products by size.
     * @param size Size to search
     * @return List of matching products
     */
    public List<Product> findBySize(String size) {
        return productsFromIndex(sizeIndex, size);
    }

    /**
     * Finds products by color and size.
     * @param color Color to search
     * @param size Size to search
     * @return List of matching products
     */
    public List<Product> findByColorAndSize(String color, String size) {
        return productsFromIndex(colorSizeIndex, composite(color, size));
    }

    /**
     * Finds products by brand and color.
     * @param brand Brand to search
     * @param color Color to search
     * @return List of matching products
     */
    public List<Product> findByBrandAndColor(Brand brand, String color) {
        return productsFromIndex(brandColorIndex, composite(brand.name(), color));
    }

    /**
     * Finds products by brand and size.
     * @param brand Brand to search
     * @param size Size to search
     * @return List of matching products
     */
    public List<Product> findByBrandAndSize(Brand brand, String size) {
        return productsFromIndex(brandSizeIndex, composite(brand.name(), size));
    }

    /**
     * Returns all products sorted by brand and id.
     * @return Sorted list of all products
     */
    public List<Product> findAll() {
        return productsByKey.values().stream()
                .sorted(productComparator())
                .collect(Collectors.toList());
    }

    /**
     * Returns the total number of products in the repository.
     * @return product count
     */
    public int count() {
        return productsByKey.size();
    }

    /**
     * Clears all products and indexes from the repository.
     */
    public void clear() {
        productsByKey.clear();
        colorIndex.clear();
        sizeIndex.clear();
        colorSizeIndex.clear();
        brandColorIndex.clear();
        brandSizeIndex.clear();
    }

    /**
     * Adds a product key to the given index for the specified field value.
     */
    private void addIndexValue(ConcurrentMap<String, Set<String>> index, String fieldValue, String productKey) {
        index.computeIfAbsent(fieldValue, ignored -> ConcurrentHashMap.newKeySet()).add(productKey);
    }

    /**
     * Retrieves products from the given index using the lookup key.
     * @return Sorted list of products, or empty if none found
     */
    private List<Product> productsFromIndex(ConcurrentMap<String, Set<String>> index, String lookupKey) {
        Set<String> productKeys = index.getOrDefault(lookupKey, Collections.emptySet());
        if (productKeys.isEmpty()) {
            return Collections.emptyList();
        }

        List<Product> products = new ArrayList<>();
        for (String productKey : productKeys) {
            Product product = productsByKey.get(productKey);
            if (product != null) {
                products.add(product);
            }
        }

        products.sort(productComparator());
        return products;
    }

    /**
     * Comparator for sorting products by brand name and then by id.
     */
    private Comparator<Product> productComparator() {
        return Comparator.comparing((Product product) -> product.getBrand().name())
                .thenComparing(Product::getId);
    }

    /**
     * Creates a composite key from two strings, separated by '#'.
     * @param first First string
     * @param second Second string
     * @return Composite key string
     */
    private String composite(String first, String second) {
        return first + "#" + second;
    }
}

