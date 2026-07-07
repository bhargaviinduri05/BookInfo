package com.bookapi.bookmanagement.service.impl;

import com.bookapi.bookmanagement.exception.FileStorageException;
import com.bookapi.bookmanagement.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Filesystem-based implementation of FileStorageService.
 * Stores files under the configured app.upload.dir directory and persists
 * only the filename/path reference in the database (not the binary data).
 */
@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg", "image/png", "image/jpg", "image/webp"
    );

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.base-url}")
    private String baseUrl;

    private Path uploadPath;

    @PostConstruct
    public void init() {
        try {
            this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(this.uploadPath);
            log.info("Upload directory initialized at: {}", this.uploadPath);
        } catch (IOException e) {
            throw new FileStorageException("Could not create upload directory: " + uploadDir, e);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("Cannot store an empty file");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new FileStorageException(
                    "Unsupported file type: " + contentType + ". Allowed types: JPEG, PNG, WEBP");
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() == null ? "cover" : file.getOriginalFilename());

        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }

        String generatedFilename = UUID.randomUUID() + extension;

        try {
            if (originalFilename.contains("..")) {
                throw new FileStorageException("Invalid file path sequence in: " + originalFilename);
            }

            Path targetPath = this.uploadPath.resolve(generatedFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Stored file: {}", generatedFilename);
            return generatedFilename;
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file " + originalFilename, e);
        }
    }

    @Override
    public void deleteFile(String filename) {
        if (!StringUtils.hasText(filename)) {
            return;
        }
        try {
            Path filePath = this.uploadPath.resolve(filename).normalize();
            Files.deleteIfExists(filePath);
            log.info("Deleted file: {}", filename);
        } catch (IOException e) {
            log.warn("Failed to delete file {}: {}", filename, e.getMessage());
        }
    }

    @Override
    public String buildFileUrl(String filename) {
        if (!StringUtils.hasText(filename)) {
            return null;
        }
        return baseUrl + "/uploads/book-covers/" + filename;
    }
}
