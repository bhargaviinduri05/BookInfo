package com.bookapi.bookmanagement.exception;

/**
 * Thrown when a requested Book resource cannot be found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
