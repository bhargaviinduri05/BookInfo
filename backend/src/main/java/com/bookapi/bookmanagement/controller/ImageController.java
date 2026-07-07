package com.bookapi.bookmanagement.controller;

import com.bookapi.bookmanagement.dto.ApiResponse;
import com.bookapi.bookmanagement.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * REST controller dedicated to directly fetching stored book cover images
 * as a binary stream. This complements the static resource handler in
 * WebConfig and is useful for clients that prefer hitting a documented
 * /api endpoint rather than the static /uploads path, and for explicitly
 * setting content-type/cache headers.
 *
 * GET /api/images/{filename}
 */
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@CrossOrigin
public class ImageController {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = detectContentType(filename);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=86400, public")
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new FileStorageException("Malformed file path for: " + filename, e);
        }
    }

    private String detectContentType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".png")) return MediaType.IMAGE_PNG_VALUE;
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return MediaType.IMAGE_JPEG_VALUE;
        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
}
