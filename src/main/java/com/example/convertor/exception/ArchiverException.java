package com.example.convertor.exception;

public class ArchiverException extends RuntimeException {

    public ArchiverException(String message) {
        super("Archiving error with message: " + message);
    }

    public ArchiverException(String message, Throwable throwable) {
        super("Archiving error with message: " + message, throwable);
    }
}
