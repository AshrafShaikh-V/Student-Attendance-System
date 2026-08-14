-- V2__indexes.sql
-- Covering indexes for the hot read paths. JPA's @Column(unique = true) emits a UNIQUE
-- constraint, which is logically a unique index, but we name them explicitly so future
-- migration scripts can reference them by name.

-- Unique indexes (some are also created by the unique constraints above; we keep these
-- so the names are stable and we can DROP them by name in future migrations).
CREATE UNIQUE INDEX uk_student_roll_number ON students(roll_number);
CREATE UNIQUE INDEX uk_student_email       ON students(email);
CREATE UNIQUE INDEX uk_teacher_email       ON teachers(email);
CREATE UNIQUE INDEX uk_subject_code        ON subjects(subject_code);

-- Covering indexes for attendance search/filter patterns.
CREATE INDEX idx_attendance_student_date  ON attendances(student_id, attendance_date);
CREATE INDEX idx_attendance_subject_date  ON attendances(subject_id, attendance_date);
CREATE INDEX idx_attendance_status        ON attendances(status);

-- Lookup by department/year for student directory queries.
CREATE INDEX idx_student_department_year  ON students(department, academic_year);
