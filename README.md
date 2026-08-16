# Student Attendance Management System

## Project Overview

This project is a Spring Boot REST API for managing student attendance records in an academic environment. It supports managing students, teachers, subjects, attendance entries, and attendance summaries without exposing database entities directly to clients.

The system is designed to be easy to consume by a frontend application while maintaining a clean separation between controllers, services, repositories, DTOs, and exception handling.

## Features

- Student CRUD operations
- Teacher CRUD operations
- Subject CRUD operations
- Attendance record creation, update, and deletion
- Attendance filtering by student, subject, date, and status
- Student attendance percentage calculation
- Subject-level and date-level attendance reporting
- Request validation with meaningful error responses
- Centralized global exception handling
- Flyway-based database migration scripts
- OpenAPI / Swagger-based API documentation

## Tech Stack

- Java 26
- Spring Boot 4.1.0
- Spring Data JPA
- Hibernate
- MySQL
- H2 (for local validation and testing)
- Maven
- Lombok
- SpringDoc OpenAPI
- Flyway

## Project Structure

The application follows a standard Spring Boot package layout:

- `com.attendance.attendance_tracker.controller` – REST controllers
- `com.attendance.attendance_tracker.service` – business logic
- `com.attendance.attendance_tracker.repository` – persistence logic
- `com.attendance.attendance_tracker.entity` – JPA entities
- `com.attendance.attendance_tracker.dto` – request and response DTOs
- `com.attendance.attendance_tracker.exception` – custom exceptions and global handler
- `com.attendance.attendance_tracker.config` – app and web configuration
- `src/main/resources/db/migration` – Flyway migration scripts

## Installation

1. Clone the repository.
2. Ensure Java 26 is installed and configured.
3. From the project root, run:

```bash
./mvnw clean install
```

4. Start the app locally:

```bash
./mvnw spring-boot:run
```

By default, the application starts on port `8080`.

## Database Setup

The application is configured to use a default local H2 database for local startup, which makes it easy to run without additional infrastructure.

For MySQL in a local environment, create a database manually:

```sql
CREATE DATABASE student_attendance;
```

Then configure the connection using environment variables such as:

```bash
export DB_URL=jdbc:mysql://localhost:3306/student_attendance?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&serverTimezone=UTC
export DB_USERNAME=root
export DB_PASSWORD=your_password
export DB_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
export JPA_HIBERNATE_DDL_AUTO=validate
export HIBERNATE_DIALECT=org.hibernate.dialect.MySQLDialect
export FLYWAY_ENABLED=true
```

## Configuration

The main configuration is managed in `src/main/resources/application.properties`.

Key settings include:

- datasource connection configuration
- JPA and Hibernate configuration
- Flyway migration configuration
- OpenAPI / Swagger configuration
- Actuator endpoints
- JWT-related settings are present for compatibility, though no auth flow is implemented in this documentation phase

## API Documentation

Swagger UI is available through SpringDoc.

Open the following URL in the browser after starting the app:

- http://localhost:8080/swagger-ui.html
- or http://localhost:8080/swagger-ui/index.html

The OpenAPI JSON is available at:

- http://localhost:8080/v3/api-docs

## Example API Requests

### Create a student

```json
{
  "rollNumber": "S-100",
  "firstName": "Alice",
  "lastName": "Brown",
  "email": "alice@example.com",
  "department": "Computer Science",
  "year": 2,
  "division": "A"
}
```

### Create a teacher

```json
{
  "firstName": "John",
  "lastName": "Miller",
  "email": "john@school.com",
  "password": "Passw0rd!",
  "specialization": "Mathematics"
}
```

### Create a subject

```json
{
  "subjectCode": "CS-101",
  "subjectName": "Computer Science",
  "credits": 4
}
```

### Create attendance

```json
{
  "studentId": 1,
  "subjectId": 1,
  "attendanceDate": "2026-08-12",
  "status": "PRESENT"
}
```

## API Endpoints

### Student API

- `POST /students`
- `GET /students`
- `GET /students/{id}`
- `PUT /students/{id}`
- `DELETE /students/{id}`
- `GET /students/search`

### Teacher API

- `POST /teachers`
- `GET /teachers`
- `GET /teachers/{id}`
- `PUT /teachers/{id}`
- `DELETE /teachers/{id}`
- `GET /teachers/search`

### Subject API

- `POST /subjects`
- `GET /subjects`
- `GET /subjects/{id}`
- `PUT /subjects/{id}`
- `DELETE /subjects/{id}`
- `GET /subjects/search`

### Attendance API

- `POST /attendance`
- `GET /attendance`
- `GET /attendance/{id}`
- `PUT /attendance/{id}`
- `DELETE /attendance/{id}`
- `GET /attendance/student/{studentId}/percentage`
- `GET /attendance/student/{studentId}/subject/{subjectId}/percentage`
- `GET /attendance/report/student/{studentId}`
- `GET /attendance/report/subject/{subjectId}`
- `GET /attendance/report/date/{date}`

## Future Improvements

Possible improvements for later phases include:

- Proper authentication and authorization
- Pagination and sorting on large datasets
- More advanced reporting filters
- Audit trails and soft delete support
- Frontend dashboard integration
- Production-grade security and deployment configuration

## Notes

This project is structured as a clean backend service for academic attendance tracking. The codebase is intended to remain maintainable, testable, and easy to document with standard Spring Boot tooling.
