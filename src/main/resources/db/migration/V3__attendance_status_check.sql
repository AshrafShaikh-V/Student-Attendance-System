-- V3__attendance_status_check.sql
-- Documents the allowed AttendanceStatus values. The column is VARCHAR, not ENUM, so this
-- is a no-op for storage but enforces a CHECK constraint at the DB level on MySQL 8+ and
-- on H2 (when tests use MySQL mode).

ALTER TABLE attendances
    ADD CONSTRAINT chk_attendance_status
    CHECK (status IN ('PRESENT','ABSENT','LATE','EXCUSED'));
