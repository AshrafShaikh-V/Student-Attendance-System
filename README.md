# Student Attendance Management System

## Project Overview

The Student Attendance Management System is a Spring Boot backend application designed for academic institutions to track, manage, and monitor student attendance efficiently. The system provides CRUD support for students, teachers, subjects, and attendance records while also calculating attendance percentages and generating attendance summaries for reporting.

The project follows a clean layered architecture based on controllers, services, repositories, DTOs, and centralized exception handling. It is designed to be easy to integrate with frontend applications and to remain maintainable for academic project demonstrations, portfolio submission, and interviews.

## Features

- Student management with validation and unique identifiers
- Teacher management with secure password handling
- Subject management with unique subject codes
- Attendance record creation, update, and deletion
- Filtered attendance queries by student, subject, date, and status
- Attendance percentage calculation per student and subject
- Attendance reporting by student, subject, and date
- JWT-based authentication and role-based authorization
- Centralized global exception handling
- OpenAPI / Swagger documentation
- Docker and MySQL support for easy deployment

## Tech Stack

- Java 26
- Spring Boot 4.1.0
- Spring Data JPA
- Hibernate ORM
- MySQL 8
- H2 database for local testing
- Maven
- Lombok
- Spring Security 6
- JWT (JJWT)
- SpringDoc OpenAPI
- Flyway
- Docker / Docker Compose

## Architecture

The project uses a standard Spring Boot layered structure:

- `controller` – REST endpoints and request handling
- `service` – business logic and validation
- `repository` – database persistence logic
- `entity` – JPA entities
- `dto` – request/response DTOs
- `exception` – custom exceptions and exception handler
- `config` – application, security, and infrastructure settings
- `security` – JWT and authentication support

## Folder Structure

```text
attendance-tracker/
├── src/
│   ├── main/
│   │   ├── java/com/attendance/attendance_tracker/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   ├── service/
│   │   │   └── AttendanceTrackerApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       └── db/migration/
│   └── test/
│       └── java/
├── Dockerfile
├── docker-compose.yml
├── application-example.properties
├── pom.xml
├── mvnw
├── .gitignore
├── .dockerignore
├── README.md
└── LICENSE
```

## Installation Guide

### Prerequisites

- Java 26
- Maven 3.9+
- MySQL 8 (for production deployment)
- Docker and Docker Compose (optional, recommended for containerized setup)

### Local Setup

1. Clone the repository:

```bash
git clone <repository-url>
cd attendance-tracker
```

2. Build the project:

```bash
./mvnw clean install
```

3. Run the application:

```bash
./mvnw spring-boot:run
```

The application starts on port `8080` by default.

## Database Setup

### MySQL Setup

Create the database in MySQL:

```sql
CREATE DATABASE student_attendance;
```

Then configure environment variables or update the example configuration:

```bash
export DB_URL=jdbc:mysql://localhost:3306/student_attendance?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&serverTimezone=UTC
export DB_USERNAME=root
export DB_PASSWORD=your_password
export DB_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
export JPA_HIBERNATE_DDL_AUTO=validate
export HIBERNATE_DIALECT=org.hibernate.dialect.MySQLDialect
export FLYWAY_ENABLED=true
```

For local quick start, the app also supports an H2 in-memory configuration by default when no database variables are provided.

## Configuration

The main Spring Boot configuration is in `src/main/resources/application.properties`.

Key settings include:

- datasource configuration
- Hibernate and JPA configuration
- Flyway setup
- JWT secret and TTL
- server port and actuator exposure
- Swagger configuration

For example values without secrets, see `application-example.properties`.

## Running the Application

### Run Locally with Maven

```bash
export JAVA_HOME=/path/to/jdk-26
./mvnw spring-boot:run
```

### Run with a Custom Profile

```bash
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
```

## Swagger Documentation

Swagger UI is available once the application is running:

- http://localhost:8080/swagger-ui.html
- http://localhost:8080/swagger-ui/index.html
- http://localhost:8080/v3/api-docs

Swagger includes the REST endpoints for students, teachers, subjects, attendance, reports, and authentication.

## Authentication Guide

The application uses JWT-based authentication.

### Public Routes

- `/auth/login`
- `/swagger-ui/**`
- `/v3/api-docs/**`

### Protected Routes

All other endpoints require a valid JWT token in the `Authorization` header:

```http
Authorization: Bearer <token>
```

### Login Example

```http
POST /auth/login
Content-Type: application/json

{
  "email": "admin@attendance.local",
  "password": "yourPassword"
}
```

## API Overview

### Student APIs

- `POST /students`
- `GET /students`
- `GET /students/{id}`
- `PUT /students/{id}`
- `DELETE /students/{id}`
- `GET /students/search`

### Teacher APIs

- `POST /teachers`
- `GET /teachers`
- `GET /teachers/{id}`
- `PUT /teachers/{id}`
- `DELETE /teachers/{id}`
- `GET /teachers/search`

### Subject APIs

- `POST /subjects`
- `GET /subjects`
- `GET /subjects/{id}`
- `PUT /subjects/{id}`
- `DELETE /subjects/{id}`
- `GET /subjects/search`

### Attendance APIs

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

## Sample Requests

### Create Student

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

### Create Subject

```json
{
  "subjectCode": "CS-101",
  "subjectName": "Computer Science",
  "credits": 4
}
```

### Create Attendance

```json
{
  "studentId": 1,
  "subjectId": 1,
  "attendanceDate": "2026-08-12",
  "status": "PRESENT"
}
```

### Login Request

```json
{
  "email": "admin@attendance.local",
  "password": "yourPassword"
}
```

## Future Improvements

Possible future enhancements include:

- Pagination and sorting support for larger datasets
- Advanced analytics dashboards
- Audit trails and soft delete support
- Email notifications and reminders
- Frontend integration with a modern UI
- CI/CD pipeline automation and deployment staging

## License

This project is intended for educational and portfolio use. Add an appropriate license before public production deployment.
