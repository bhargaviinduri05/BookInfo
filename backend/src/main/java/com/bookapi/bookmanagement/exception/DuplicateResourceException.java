package com.bookapi.bookmanagement.exception;

/**
 * Thrown when attempting to create a Book with an ISBN that already exists.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
