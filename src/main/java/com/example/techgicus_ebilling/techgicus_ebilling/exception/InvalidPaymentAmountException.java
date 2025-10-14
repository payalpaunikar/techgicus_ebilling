package com.example.techgicus_ebilling.techgicus_ebilling.exception;

public class InvalidPaymentAmountException extends RuntimeException{
    public InvalidPaymentAmountException(String message) {
        super(message);
    }
}
