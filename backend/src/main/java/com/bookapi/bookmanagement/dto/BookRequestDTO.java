package com.bookapi.bookmanagement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO used for incoming create/update book requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Author must not exceed 255 characters")
    private String author;

    @NotBlank(message = "ISBN is required")
    @Pattern(
            regexp = "^(97(8|9))?\\d{9}(\\d|X)$|^[\\d-]{10,17}$",
            message = "ISBN must be a valid ISBN-10 or ISBN-13 format"
    )
    private String isbn;

    @Size(max = 255, message = "Publisher must not exceed 255 characters")
    private String publisher;

    @Size(max = 100, message = "Genre must not exceed 100 characters")
    private String genre;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be zero or positive")
    private Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be zero or positive")
    private Integer quantity;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;
}
