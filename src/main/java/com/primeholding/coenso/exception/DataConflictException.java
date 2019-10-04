package com.primeholding.coenso.exception;

public class DataConflictException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataConflictException(String message) {
        super(message);
    }
}
