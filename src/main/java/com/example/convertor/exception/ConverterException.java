package com.example.convertor.exception;

public class ConverterException extends RuntimeException {

    public ConverterException(String message) {
        super("Converting error with message: " + message);
    }

    public ConverterException(String message, Throwable throwable) {
        super("Converting error with message: " + message, throwable);
    }
}
