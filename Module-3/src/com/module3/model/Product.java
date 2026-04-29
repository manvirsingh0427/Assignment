package com.module3.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a product with id, brand, color, size, type, and optional price.
 * Immutable value object.
 */
public final class Product {
    private final String id;
    private final Brand brand;
    private final String color;
    private final String size;
    private final ProductType type;
    private final BigDecimal price;

    /**
     * Constructs a Product instance.
     * @param id Product identifier
     * @param brand Product brand
     * @param color Product color
     * @param size Product size
     * @param type Product type
     * @param price Product price (nullable)
     */
    public Product(String id, Brand brand, String color, String size, ProductType type, BigDecimal price) {
        this.id = id;
        this.brand = brand;
        this.color = color;
        this.size = size;
        this.type = type;
        this.price = price;
    }

    /** @return Product identifier */
    public String getId() {
        return id;
    }

    /** @return Product brand */
    public Brand getBrand() {
        return brand;
    }

    /** @return Product color */
    public String getColor() {
        return color;
    }

    /** @return Product size */
    public String getSize() {
        return size;
    }

    /** @return Product type */
    public ProductType getType() {
        return type;
    }

    /** @return Product price (nullable) */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @return true if product has a price, false otherwise
     */
    public boolean hasPrice() {
        return price != null;
    }

    /**
     * Returns a unique key for the product based on id and brand.
     * @return Unique product key
     */
    public String uniqueKey() {
        return id + "#" + brand.name();
    }

    /**
     * Returns a display string for the product.
     * @return Human-readable product string
     */
    public String toDisplayString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id = ").append(id)
                .append(", brand = ").append(brand.name())
                .append(", color = ").append(color)
                .append(", size = ").append(size)
                .append(", type = ").append(type.name());

        if (hasPrice()) {
            builder.append(", price = ").append(price.stripTrailingZeros().toPlainString());
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Product)) {
            return false;
        }
        Product product = (Product) other;
        return Objects.equals(id, product.id) && brand == product.brand;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand);
    }
}
