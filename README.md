# Order Processing Service

A Spring Boot service for managing orders with async queue-based processing.

## Prerequisites

- Java 21
- Maven (or use the included `./mvnw` wrapper)

## Commands

| Command | Description |
|---------|-------------|
| `./mvnw spring-boot:run` | Start the application |
| `./mvnw test` | Run all tests (uses H2 in-memory DB, no setup required) |
| `./mvnw package -DskipTests` | Package the application without running tests |
| `./mvnw clean install` | Full build including all tests |
