# Webshop Processor Application

## Overview

This Spring Boot application processes webshop transaction data using Spring Batch, loads the data into an in-memory H2 database, and generates reports based on the database content. The reports are saved in the `result` directory located in the project root.

## Running the Application
1. **Use Java version 21**
2. **Start the Application**:
    - Run the application using Maven:
      ```bash
      mvn spring-boot:run
      ```
    - Or run the compiled JAR file:
      ```bash
      mvn clean install
      java -jar target/webshop-processor-0.0.1-SNAPSHOT.jar
      ```

3. **Result**:
    - After running the application, the generated CSV reports can be found in the `result` directory:
        - `report01.csv`: Customer report.
        - `top.csv`: Top customers report.
        - `report02.csv`: Webshop report.

## Important

- The H2 database runs in memory, so it will be re-initialized each time the application starts.
---