-- V1__baseline.sql
-- Baseline schema for the Student Attendance System.
-- Replaces the previous ad-hoc Hibernate-generated DDL with an explicit, reviewable definition.

CREATE TABLE IF NOT EXISTS students (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    roll_number     VARCHAR(20)     NOT NULL,
    first_name      VARCHAR(50)     NOT NULL,
    last_name       VARCHAR(50)     NOT NULL,
    email           VARCHAR(100)    NOT NULL,
    department      VARCHAR(50)     NOT NULL,
    academic_year   INT             NOT NULL,
    division        VARCHAR(10)     NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS teachers (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    first_name      VARCHAR(50)     NOT NULL,
    last_name       VARCHAR(50)     NOT NULL,
    email           VARCHAR(100)    NOT NULL,
    password        VARCHAR(100)    NOT NULL,
    specialization  VARCHAR(100)    NULL,
    role            VARCHAR(20)     NOT NULL DEFAULT 'TEACHER',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS subjects (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    subject_code    VARCHAR(20)     NOT NULL,
    subject_name    VARCHAR(100)    NOT NULL,
    credits         INT             NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS attendances (
    id               BIGINT         NOT NULL AUTO_INCREMENT,
    student_id       BIGINT         NOT NULL,
    subject_id       BIGINT         NOT NULL,
    attendance_date  DATE           NOT NULL,
    status           VARCHAR(20)    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_attendance_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_attendance_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
    CONSTRAINT uk_student_subject_date UNIQUE (student_id, subject_id, attendance_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
