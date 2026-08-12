package com.attendance.attendance_tracker.exception;

public class DuplicateAttendanceException extends DuplicateResourceException {

    private static final long serialVersionUID = 1L;

    public DuplicateAttendanceException(String message) {
        super(message);
    }
}
