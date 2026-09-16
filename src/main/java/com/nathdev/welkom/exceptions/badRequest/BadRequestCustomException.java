package com.nathdev.welkom.exceptions.badRequest;

public class BadRequestCustomException extends RuntimeException {
    public BadRequestCustomException(String message) {
        super(message);
    }
}
