package com.bookapi.bookmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Exposes the upload directory as a static resource path so that
 * stored book cover images can be retrieved directly via HTTP GET,
 * e.g. GET /uploads/book-covers/<filename>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = new File(uploadDir).getAbsolutePath();
        registry.addResourceHandler("/uploads/book-covers/**")
                .addResourceLocations("file:" + absolutePath + File.separator);
    }
}
