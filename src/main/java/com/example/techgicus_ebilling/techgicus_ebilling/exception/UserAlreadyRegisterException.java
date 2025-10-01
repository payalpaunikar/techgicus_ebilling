package com.example.techgicus_ebilling.techgicus_ebilling.exception;

public class UserAlreadyRegisterException extends RuntimeException{
    public UserAlreadyRegisterException(String message) {
        super(message);
    }
}
