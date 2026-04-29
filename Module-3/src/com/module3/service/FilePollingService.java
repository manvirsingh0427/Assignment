package com.module3.service;

import com.module3.io.BrandCsvReader;
import com.module3.io.CsvReaderFactory;
import com.module3.model.Brand;
import com.module3.model.Product;
import com.module3.store.ProductRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilePollingService {
    private final Path inputDirectory;
    private final ProductRepository repository;
    private final Map<Brand, BrandCsvReader> readers = new EnumMap<>(Brand.class);
    private final Map<Brand, Set<Path>> processedFiles = new EnumMap<>(Brand.class);
    private final Consumer<String> logger;
    private final ScheduledExecutorService executorService;
    private volatile boolean started;

    public FilePollingService(Path inputDirectory,
                              ProductRepository repository,
                              CsvReaderFactory readerFactory,
                              Consumer<String> logger) {
        this(inputDirectory, repository, readerFactory, logger, Executors.newScheduledThreadPool(2));
    }

    public FilePollingService(Path inputDirectory,
                              ProductRepository repository,
                              CsvReaderFactory readerFactory,
                              Consumer<String> logger,
                              ScheduledExecutorService executorService) {
        this.inputDirectory = inputDirectory;
        this.repository = repository;
        this.logger = logger;
        this.executorService = executorService;
        this.readers.put(Brand.PUMA, readerFactory.getReader(Brand.PUMA));
        this.readers.put(Brand.NIKE, readerFactory.getReader(Brand.NIKE));
        this.processedFiles.put(Brand.PUMA, java.util.concurrent.ConcurrentHashMap.newKeySet());
        this.processedFiles.put(Brand.NIKE, java.util.concurrent.ConcurrentHashMap.newKeySet());
    }

    public synchronized void start() {
        if (started) {
            return;
        }

        ensureInputDirectoryExists();
        started = true;

        Future<?> pumaStartup = executorService.submit(() -> safelyProcessBrand(Brand.PUMA));
        Future<?> nikeStartup = executorService.submit(() -> safelyProcessBrand(Brand.NIKE));
        waitForInitialLoad(pumaStartup, nikeStartup);
        executorService.scheduleAtFixedRate(() -> safelyProcessBrand(Brand.PUMA), 30, 30, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(() -> safelyProcessBrand(Brand.NIKE), 30, 30, TimeUnit.SECONDS);
    }

    public synchronized void stop() {
        started = false;
        executorService.shutdownNow();
    }

    public void scanAllNow() {
        ensureInputDirectoryExists();
        safelyProcessBrand(Brand.PUMA);
        safelyProcessBrand(Brand.NIKE);
    }

    public void scanBrandNow(Brand brand) {
        ensureInputDirectoryExists();
        safelyProcessBrand(brand);
    }

    private void safelyProcessBrand(Brand brand) {
        try {
            int addedCount = processNewFiles(brand);
            if (addedCount > 0) {
                logger.accept(String.format("Loaded %d new %s record(s). Total records in memory: %d", addedCount, brand.name(), repository.count()));
            }
        } catch (Exception ex) {
            logger.accept("Failed to process files for brand " + brand.name() + ": " + ex.getMessage());
        }
    }

    private int processNewFiles(Brand brand) throws IOException {
        BrandCsvReader reader = readers.get(brand);
        List<Path> pendingFiles = listMatchingFiles(reader).stream()
                .filter(path -> !processedFiles.get(brand).contains(path.toAbsolutePath().normalize()))
                .collect(Collectors.toList());

        int totalAdded = 0;
        for (Path filePath : pendingFiles) {
            List<Product> products = reader.readProducts(filePath);
            totalAdded += repository.addProducts(products);
            processedFiles.get(brand).add(filePath.toAbsolutePath().normalize());
        }
        return totalAdded;
    }

    private List<Path> listMatchingFiles(BrandCsvReader reader) throws IOException {
        if (!Files.exists(inputDirectory)) {
            return new ArrayList<>();
        }

        try (Stream<Path> stream = Files.list(inputDirectory)) {
            return stream.filter(Files::isRegularFile)
                    .filter(reader::supports)
                    .sorted(Comparator.comparing(path -> path.getFileName().toString().toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        }
    }

    private void ensureInputDirectoryExists() {
        try {
            Files.createDirectories(inputDirectory);
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to access input directory: " + inputDirectory, ex);
        }
    }

    private void waitForInitialLoad(Future<?> pumaStartup, Future<?> nikeStartup) {
        try {
            pumaStartup.get();
            nikeStartup.get();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed during initial file loading.", ex);
        }
    }
}

