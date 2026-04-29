package com.module3.cli;

import com.module3.model.FilterOption;
import com.module3.model.Product;
import com.module3.service.FilePollingService;
import com.module3.service.ProductService;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class ConsoleApplication {
    private final ProductService productService;
    private final FilePollingService filePollingService;
    private final Scanner scanner;
    private final PrintStream output;

    public ConsoleApplication(ProductService productService,
                              FilePollingService filePollingService,
                              Scanner scanner,
                              PrintStream output) {
        this.productService = productService;
        this.filePollingService = filePollingService;
        this.scanner = scanner;
        this.output = output;
    }

    public void start() {
        filePollingService.start();
        output.println("Product loader started. CSV files are read from the input folder every 30 seconds.");
        output.println("Currently loaded records: " + productService.totalProducts());
        output.println();

        while (true) {
            printMenu();
            if (!scanner.hasNextLine()) {
                output.println();
                output.println("Input stream closed. Exiting application.");
                filePollingService.stop();
                return;
            }
            String optionValue = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(optionValue)) {
                output.println("Exiting application. Goodbye!");
                filePollingService.stop();
                return;
            }

            try {
                FilterOption filterOption = FilterOption.fromMenuValue(optionValue);
                List<Product> results = processSearch(filterOption);
                output.println(productService.formatResults(results));
            } catch (IllegalArgumentException ex) {
                output.println("Error: " + ex.getMessage());
            } catch (Exception ex) {
                output.println("Something went wrong while processing your request: " + ex.getMessage());
            }

            output.println();
        }
    }

    private List<Product> processSearch(FilterOption filterOption) {
        switch (filterOption) {
            case COLOR:
                return productService.search(filterOption, ask("Enter COLOR"), null);
            case SIZE:
                return productService.search(filterOption, ask("Enter SIZE"), null);
            case COLOR_AND_SIZE:
                return productService.search(filterOption, ask("Enter COLOR"), ask("Enter SIZE"));
            case BRAND_AND_COLOR:
                return productService.search(filterOption, ask("Enter BRAND (PUMA/NIKE)"), ask("Enter COLOR"));
            case BRAND_AND_SIZE:
                return productService.search(filterOption, ask("Enter BRAND (PUMA/NIKE)"), ask("Enter SIZE"));
            default:
                throw new IllegalArgumentException("Unsupported option selected.");
        }
    }

    private void printMenu() {
        output.println("Choose a filter option or type 'exit':");
        output.println("1. COLOR");
        output.println("2. SIZE");
        output.println("3. COLOR AND SIZE");
        output.println("4. BRAND AND COLOR");
        output.println("5. BRAND AND SIZE");
        output.print("Your choice: ");
    }

    private String ask(String label) {
        output.print(label + ": ");
        if (!scanner.hasNextLine()) {
            throw new IllegalArgumentException("Input was not provided. Please try again.");
        }
        return scanner.nextLine();
    }
}

