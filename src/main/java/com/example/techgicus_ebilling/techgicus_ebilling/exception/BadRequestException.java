package com.example.techgicus_ebilling.techgicus_ebilling.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
