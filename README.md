# Student Attendance System

A robust, enterprise-grade RESTful web application built with **Spring Boot 3**, **Spring Data JPA**, and **H2 Database** for managing student attendance, teachers, and academic subjects.

## 🚀 Features

- **Student Management**: Full CRUD operations with unique email validation and formatted roll number assignment.
- **Teacher Management**: Complete teacher profiles with subject assignment capabilities and duplicate checks.
- **Subject Module**: Academic subject cataloging with unique subject code verification.
- **Attendance Tracking**: Comprehensive attendance logging with dynamic status support (`PRESENT`, `ABSENT`, `LATE`, `EXCUSED`).
- **Global Exception Handling**: Centralized handling for custom domain exceptions with consistent JSON error responses.

## 🛠️ Tech Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA & Hibernate**
- **H2 In-Memory Database**
- **Lombok**
- **Maven**

## 📋 API Endpoints

### Student API (`/api/students`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/students` | Create a new student profile |
| `GET` | `/api/students` | Retrieve all registered students |
| `GET` | `/api/students/{id}` | Get student details by ID |
| `PUT` | `/api/students/{id}` | Update existing student profile |
| `DELETE` | `/api/students/{id}` | Delete a student profile |

### Teacher API (`/api/teachers`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/teachers` | Register a new teacher |
| `GET` | `/api/teachers` | List all teachers |
| `GET` | `/api/teachers/{id}` | Retrieve teacher profile by ID |
| `PUT` | `/api/teachers/{id}` | Update teacher details |
| `DELETE` | `/api/teachers/{id}` | Delete a teacher profile |

### Subject API (`/api/subjects`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/subjects` | Create a new subject entry |
| `GET` | `/api/subjects` | Fetch all subjects |
| `GET` | `/api/subjects/{id}` | Get subject details by ID |
| `PUT` | `/api/subjects/{id}` | Update subject information |
| `DELETE` | `/api/subjects/{id}` | Remove a subject |

### Attendance API (`/attendance`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/attendance` | Create attendance for a student and subject |
| `GET` | `/attendance` | List attendance records, optionally filtered by `studentId`, `subjectId`, `attendanceDate`, or `status` |
| `GET` | `/attendance/{id}` | Retrieve a single attendance record by ID |
| `PUT` | `/attendance/{id}` | Update an attendance record, preventing duplicates by student, subject, and date |
| `DELETE` | `/attendance/{id}` | Delete an attendance record |

> Validation:
> - `studentId`, `subjectId`, `attendanceDate`, and `status` are required.
> - `attendanceDate` cannot be in the future.
> - Duplicate attendance for the same student, subject, and date is rejected with `409 CONFLICT`.

## 🧪 Running the Application & Tests

```bash
# Compile and test
./mvnw clean test

# Run locally
./mvnw spring-boot:run
```
