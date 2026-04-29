package com.module3.service;

import com.module3.model.FilterOption;
import com.module3.model.Product;
import com.module3.store.ProductRepository;
import com.module3.validation.ProductValidator;

import java.util.List;
import java.util.stream.IntStream;

public class ProductService {
    private final ProductRepository repository;
    private final ProductValidator validator;

    public ProductService(ProductRepository repository, ProductValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public List<Product> search(FilterOption filterOption, String firstValue, String secondValue) {
        switch (filterOption) {
            case COLOR:
                return repository.findByColor(validator.normalizeFilterValue(firstValue, "COLOR"));
            case SIZE:
                return repository.findBySize(validator.normalizeSize(firstValue));
            case COLOR_AND_SIZE:
                return repository.findByColorAndSize(
                        validator.normalizeFilterValue(firstValue, "COLOR"),
                        validator.normalizeSize(secondValue)
                );
            case BRAND_AND_COLOR:
                return repository.findByBrandAndColor(
                        validator.parseBrand(firstValue),
                        validator.normalizeFilterValue(secondValue, "COLOR")
                );
            case BRAND_AND_SIZE:
                return repository.findByBrandAndSize(
                        validator.parseBrand(firstValue),
                        validator.normalizeSize(secondValue)
                );
            default:
                throw new IllegalArgumentException("Unsupported filter option selected.");
        }
    }

    public String formatResults(List<Product> products) {
        if (products.isEmpty()) {
            return "No items found for the given filter.";
        }

        StringBuilder builder = new StringBuilder();
        IntStream.range(0, products.size())
                .forEach(index -> builder.append("item")
                        .append(index + 1)
                        .append(" -> ")
                        .append(products.get(index).toDisplayString())
                        .append(System.lineSeparator()));
        return builder.toString().trim();
    }

    public int totalProducts() {
        return repository.count();
    }
}

