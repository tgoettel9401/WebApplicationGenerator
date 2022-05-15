package com.example.simpledataapplication.util;

public class NotFoundException extends Exception {
    public NotFoundException(String title, Long id) {
        super(title + "with ID " + id + " has not been found");
    }
}
