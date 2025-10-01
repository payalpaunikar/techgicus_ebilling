package com.example.techgicus_ebilling.techgicus_ebilling.exception;

public class UserSubscriptionNotActiveException extends RuntimeException{
    public UserSubscriptionNotActiveException(String message) {
        super(message);
    }
}
