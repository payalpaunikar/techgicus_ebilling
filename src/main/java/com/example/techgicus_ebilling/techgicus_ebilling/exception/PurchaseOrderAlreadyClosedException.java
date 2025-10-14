package com.example.techgicus_ebilling.techgicus_ebilling.exception;

public class PurchaseOrderAlreadyClosedException extends RuntimeException{
    public PurchaseOrderAlreadyClosedException(String message) {
        super(message);
    }
}
