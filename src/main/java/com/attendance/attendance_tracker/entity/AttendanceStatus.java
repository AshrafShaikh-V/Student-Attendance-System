package com.attendance.attendance_tracker.entity;

/**
 * Attendance status for a student on a given date.
 * Adding new values is non-breaking because the column is stored as a STRING.
 */
public enum AttendanceStatus {
    PRESENT,
    ABSENT,
    LATE,
    EXCUSED
}
