package com.attendance.attendance_tracker.exception;

public class DuplicateStudentException extends DuplicateResourceException {

    private static final long serialVersionUID = 1L;

    public DuplicateStudentException(String message) {
        super(message);
    }
}
