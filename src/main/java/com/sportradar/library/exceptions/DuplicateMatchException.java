package com.sportradar.library.exceptions;

public class DuplicateMatchException extends RuntimeException {
    public DuplicateMatchException(String message) {
        super(message);
    }
}