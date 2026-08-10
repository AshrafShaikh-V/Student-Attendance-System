package com.attendance.attendance_tracker.exception;

public class TeacherNotFoundException extends ResourceNotFoundException {

    private static final long serialVersionUID = 1L;

    public TeacherNotFoundException(String message) {
        super(message);
    }

    public TeacherNotFoundException(Long id) {
        super("Teacher not found with id: " + id);
    }
}
