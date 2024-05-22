package com.example.MyBookShopApp.customErrors;

public class EmptySearchException extends Exception {
    public EmptySearchException(String message) {
        super(message);
    }
}
