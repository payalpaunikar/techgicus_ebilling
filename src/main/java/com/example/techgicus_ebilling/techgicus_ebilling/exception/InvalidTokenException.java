package com.example.techgicus_ebilling.techgicus_ebilling.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends RuntimeException{
    private final HttpStatus status;

    public InvalidTokenException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
