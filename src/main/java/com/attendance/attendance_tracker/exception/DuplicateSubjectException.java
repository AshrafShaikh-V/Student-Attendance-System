package com.attendance.attendance_tracker.exception;

public class DuplicateSubjectException extends DuplicateResourceException {

    private static final long serialVersionUID = 1L;

    public DuplicateSubjectException(String message) {
        super(message);
    }
}
