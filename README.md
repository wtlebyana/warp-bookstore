# Warp Bookstore Management

A Spring Boot application for managing books, built with:

- Java 17+
- Spring Boot 3.5.0
- PostgreSQL (default `postgres` database)
- JPA / Hibernate
- Jakarta Validation
- Lombok
- Swagger (OpenAPI 3)
---

## Features

- Create, update, findById and delete books
- Unique ISBN auto-generation with check digit
- Validation on inputs
- RESTful API with JSON
- Swagger UI for API testing
- Initialization of book creation on startup

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- PostgreSQL installed and running locally

---

## 🧪 Running the Application Locally

### 1. Clone the Repository

```bash
git clone https://github.com/wtlebyana/warp-bookstore.git
cd warp-bookstore

```
## Configuration

- The application uses PostgreSQL's default `postgres` database.
- NB: Update these in `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true

```

Running the Application 
## Build and Run with Maven
- cd warp-bookstore, then run
- mvn clean install

- mvn spring-boot:run

## Build a Jar and Run
- mvn clean package

Then run it with:
- java -jar target/warp-bookstore-0.0.1-SNAPSHOT.jar

## Access the Application

- The REST API will be available at:
  - http://localhost:8080/api/v1/

## Swagger UI for interactive API documentation is at:
  - http://localhost:8080/swagger-ui/index.html