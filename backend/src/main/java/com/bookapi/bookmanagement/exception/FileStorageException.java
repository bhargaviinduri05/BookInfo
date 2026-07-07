package com.bookapi.bookmanagement.exception;

/**
 * Thrown when an error occurs storing or retrieving a file (book cover image).
 */
public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
