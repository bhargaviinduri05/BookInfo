package com.bookapi.bookmanagement.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service responsible for storing and retrieving book cover image files
 * on the local filesystem.
 */
public interface FileStorageService {

    /**
     * Stores the given file on disk under a unique generated filename.
     *
     * @param file the multipart file to store
     * @return the stored filename (not the full path) which can be persisted on the Book entity
     */
    String storeFile(MultipartFile file);

    /**
     * Deletes a previously stored file given its filename.
     *
     * @param filename the filename to delete
     */
    void deleteFile(String filename);

    /**
     * Builds the publicly accessible URL for a stored file.
     *
     * @param filename the stored filename
     * @return the full URL, or null if filename is null/blank
     */
    String buildFileUrl(String filename);
}
