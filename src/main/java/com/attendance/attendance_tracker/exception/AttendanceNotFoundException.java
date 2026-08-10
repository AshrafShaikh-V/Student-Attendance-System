package com.attendance.attendance_tracker.exception;

public class AttendanceNotFoundException extends ResourceNotFoundException {

    public AttendanceNotFoundException(String message) {
        super(message);
    }

    public AttendanceNotFoundException(Long id) {
        super("Attendance record not found with id: " + id);
    }
}
