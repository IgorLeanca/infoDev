package com.simple.democrudapp;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Book not found with id " + id);
    }
}
