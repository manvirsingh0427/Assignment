# Product Search Application

A robust Core Java + Maven application for real-time product ingestion, search, and management, designed for learning and practical use.

---

## Overview

This application continuously monitors an input directory for new product CSV files from two brands: **PUMA** and **NIKE**. It processes these files in real-time using background threads, stores product data in an optimized in-memory repository, and provides a command-line interface for searching products with various filters.

---

## Features

- **Automatic File Polling:**  
  Two background threads (one for PUMA, one for NIKE) scan the input directory every 30 seconds for new CSV files.
- **Brand-Specific Parsing:**  
  Each brand’s files are parsed according to their format and validated.
- **In-Memory Data Store:**  
  Products are stored in a thread-safe, singleton repository (`ProductRepository`) for fast, concurrent access.
- **Duplicate Prevention:**  
  Products are uniquely identified by `ID + Brand`. Duplicate entries are ignored.
- **Flexible Search:**  
  Search products by:
  - Color
  - Size
  - Color and Size
  - Brand and Color
  - Brand and Size
- **Robust Error Handling:**  
  Invalid rows and files are skipped with clear error messages.
- **Extensible Design:**  
  Uses Factory and Singleton patterns for easy maintenance and extension.
- **Testing & Docker Support:**  
  Includes JUnit 5 tests and Dockerfile for containerized deployment.

---

## Project Structure

```
Module-3/
├── src/                # Main application source code
│   └── com/module3/
│       ├── app/        # Application launcher
│       ├── cli/        # Command-line interface
│       ├── io/         # CSV readers and factory
│       ├── model/      # Domain models (Product, Brand, etc.)
│       ├── service/    # File polling and product services
│       ├── store/      # In-memory repository
│       └── validation/ # Product validation logic
├── test/               # JUnit test cases
├── input/              # Input CSV files (watched by the app)
├── Dockerfile          # For building Docker images
├── pom.xml             # Maven build file
└── README.md           # Project documentation
```

---

## How It Works

1. **Startup:**  
   - The application starts two scheduled threads (one for each brand).
   - Each thread scans the `input/` directory for new files matching its brand (e.g., `puma-*.csv`, `nike-*.csv`).

2. **Polling:**  
   - Every 30 seconds, each thread checks for new files.
   - New files are parsed and validated; products are added to the in-memory repository if not already present.

3. **In-Memory Repository:**  
   - All products are stored in a thread-safe singleton (`ProductRepository`), ensuring fast reads and no duplicates.

4. **Searching:**  
   - The CLI allows users to search products using various filters.
   - Results are displayed in a user-friendly format.

---

## CSV File Format

### PUMA Files
- Filename: `puma-*.csv`
- Columns: `ID,Brand,COLOR,SIZE,TYPE`

### NIKE Files
- Filename: `nike-*.csv`
- Columns: `ID,Brand,COLOR,SIZE,TYPE,PRICE`

**Rules:**
- `ID + Brand` must be unique.
- `TYPE` must be `SHIRT` or `SHOE`.
- `PRICE` (for NIKE) must be a positive number.

---

## Example Input Files

- `input/puma-1.csv`
- `input/Nike-1.csv`

You can add more files (e.g., `puma-2.csv`, `nike-2.csv`) at any time. The application will process them in the next polling cycle.

---

## Running the Application

### With Maven

**Run tests:**
```powershell
mvn test
```

**Build and run:**
```powershell
mvn clean package
java -jar target/module3-1.0-SNAPSHOT.jar
```

**Specify a custom input folder:**
```powershell
java -jar target/module3-1.0-SNAPSHOT.jar C:\path\to\input
```

### In IntelliJ IDEA

- Open the project.
- Run the `Main` class from `src/Main.java`.

### With Docker

**Build the Docker image:**
```powershell
docker build -t module3-app .
```

**Run the container:**
```powershell
docker run -it module3-app
```
*(Mount your local input folder to the container for file polling.)*

---

## Example Search Usage

1. Start the application.
2. Choose a filter (e.g., `3. COLOR AND SIZE`).
3. Enter filter values (e.g., `red`, `10`).
4. See results:
   ```
   item1 -> id = 1, brand = NIKE, color = red, size = 10, type = SHOE, price = 1000
   item2 -> id = 1, brand = PUMA, color = red, size = 10, type = SHOE
   ```

---

## Notes

- Invalid or duplicate records are safely ignored.
- The application is robust against invalid user input and file errors.
- All data is kept in memory for fast access; no external database is required.

---

