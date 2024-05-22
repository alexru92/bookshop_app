package com.example.MyBookShopApp.customErrors;

public class BookstoreApiWrongParameterException extends Exception {
    public BookstoreApiWrongParameterException(String message) {
        super(message);
    }
}
