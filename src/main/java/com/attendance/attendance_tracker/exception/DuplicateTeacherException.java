package com.attendance.attendance_tracker.exception;

public class DuplicateTeacherException extends DuplicateResourceException {

    private static final long serialVersionUID = 1L;

    public DuplicateTeacherException(String message) {
        super(message);
    }
}
