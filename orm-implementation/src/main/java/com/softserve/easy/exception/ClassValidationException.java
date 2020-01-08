package com.softserve.easy.exception;

public class ClassValidationException extends  OrmException {
    public ClassValidationException() {
    }

    public ClassValidationException(String message) {
        super(message);
    }

    public ClassValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
