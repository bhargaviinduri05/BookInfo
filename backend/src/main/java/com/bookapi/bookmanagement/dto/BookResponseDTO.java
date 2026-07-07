package com.bookapi.bookmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO used for outgoing book responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDTO {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String genre;
    private Double price;
    private Integer quantity;
    private String description;

    /** Fully qualified URL the frontend can use directly in an <img> tag. */
    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
