package com.example.techgicus_ebilling.techgicus_ebilling.exception;

public class QuotationAlreadyClosedException extends RuntimeException{
    public QuotationAlreadyClosedException(String message) {
        super(message);
    }
}
