# Demo CRUD App

Minimal Spring Boot REST API providing CRUD operations for a `Book` entity.

## Requirements
- Java 21 (set `JAVA_HOME` accordingly)
- Maven

## Build
```bash
mvn clean package
```

## Run
```bash
mvn spring-boot:run
```
Application starts on `http://localhost:8080` with H2 console at `http://localhost:8080/h2-console`.

## API
Base path: `/api/books`

### List books
```bash
curl -X GET http://localhost:8080/api/books
```

### Get book by id
```bash
curl -X GET http://localhost:8080/api/books/1
```

### Create book
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Spring in Action","author":"Craig Walls","year":2023}'
```

### Update book
```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated Title","author":"New Author","year":2024}'
```

### Delete book
```bash
curl -X DELETE http://localhost:8080/api/books/1
```
