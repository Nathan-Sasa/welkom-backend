package com.nathdev.welkom.exceptions.exist;

public class AlreadyExistCustomException extends RuntimeException {
    public AlreadyExistCustomException(String message) {
        super(message);
    }
}
