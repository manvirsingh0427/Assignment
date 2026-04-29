import com.module3.cli.ConsoleApplication;
import com.module3.io.CsvReaderFactory;
import com.module3.service.FilePollingService;
import com.module3.service.ProductService;
import com.module3.store.ProductRepository;
import com.module3.validation.ProductValidator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public final class ApplicationLauncher {
    private ApplicationLauncher() {
    }

    public static void launch(String[] args) {
        ProductValidator validator = new ProductValidator();
        ProductRepository repository = ProductRepository.getInstance();
        CsvReaderFactory readerFactory = new CsvReaderFactory(validator, System.err::println);

        Path inputDirectory = resolveInputDirectory(args);
        FilePollingService filePollingService = new FilePollingService(
                inputDirectory,
                repository,
                readerFactory,
                System.out::println
        );

        ProductService productService = new ProductService(repository, validator);
        ConsoleApplication consoleApplication = new ConsoleApplication(
                productService,
                filePollingService,
                new Scanner(System.in),
                System.out
        );

        Runtime.getRuntime().addShutdownHook(new Thread(filePollingService::stop));
        consoleApplication.start();
    }

    private static Path resolveInputDirectory(String[] args) {
        if (args != null && args.length > 0 && args[0] != null && !args[0].trim().isEmpty()) {
            return Paths.get(args[0].trim()).toAbsolutePath().normalize();
        }
        return Paths.get("input").toAbsolutePath().normalize();
    }
}

