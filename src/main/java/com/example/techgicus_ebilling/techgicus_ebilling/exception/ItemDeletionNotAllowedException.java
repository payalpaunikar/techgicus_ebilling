package com.example.techgicus_ebilling.techgicus_ebilling.exception;

public class ItemDeletionNotAllowedException extends RuntimeException{
    public ItemDeletionNotAllowedException(String message) {
        super(message);
    }
}
