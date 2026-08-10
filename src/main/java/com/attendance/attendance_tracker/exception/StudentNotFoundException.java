package com.attendance.attendance_tracker.exception;

public class StudentNotFoundException extends ResourceNotFoundException {

    private static final long serialVersionUID = 1L;

    public StudentNotFoundException(String message) {
        super(message);
    }

    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }
}
