package com.attendance.attendance_tracker.exception;

public class SubjectNotFoundException extends ResourceNotFoundException {

    public SubjectNotFoundException(String message) {
        super(message);
    }

    public SubjectNotFoundException(Long id) {
        super("Subject not found with id: " + id);
    }
}
