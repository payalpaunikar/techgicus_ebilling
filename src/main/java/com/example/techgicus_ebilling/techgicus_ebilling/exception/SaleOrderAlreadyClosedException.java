package com.example.techgicus_ebilling.techgicus_ebilling.exception;

public class SaleOrderAlreadyClosedException extends RuntimeException{
    public SaleOrderAlreadyClosedException(String message) {
        super(message);
    }
}
