package com.attendance.attendance_tracker.exception;

public class AttendanceNotFoundException extends ResourceNotFoundException {

    private static final long serialVersionUID = 1L;

    public AttendanceNotFoundException(String message) {
        super(message);
    }

    public AttendanceNotFoundException(Long id) {
        super("Attendance record not found with id: " + id);
    }
}
