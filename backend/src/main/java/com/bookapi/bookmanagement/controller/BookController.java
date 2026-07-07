package com.bookapi.bookmanagement.controller;

import com.bookapi.bookmanagement.dto.ApiResponse;
import com.bookapi.bookmanagement.dto.BookRequestDTO;
import com.bookapi.bookmanagement.dto.BookResponseDTO;
import com.bookapi.bookmanagement.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller exposing all Book Management endpoints.
 *
 * Base path: /api/books
 *
 *  POST   /api/books               -> create a new book (multipart: data + image)
 *  GET    /api/books                -> retrieve all books
 *  GET    /api/books/isbn/{isbn}    -> retrieve a book by ISBN
 *  GET    /api/books/{id}           -> retrieve a book by id
 *  PUT    /api/books/{id}           -> update a book (multipart: data + optional image)
 *  DELETE /api/books/{id}           -> delete a book
 *  POST   /api/books/{id}/image     -> upload/replace a book's cover image
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@CrossOrigin
public class BookController {

    private final BookService bookService;
    private final ObjectMapper objectMapper;

    /**
     * Create a new book. Accepts multipart/form-data with:
     *  - "book": JSON string representing BookRequestDTO
     *  - "image": optional image file
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BookResponseDTO>> createBook(
            @RequestPart("book") String bookJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        BookRequestDTO requestDTO = objectMapper.readValue(bookJson, BookRequestDTO.class);
        validateManually(requestDTO);

        BookResponseDTO created = bookService.createBook(requestDTO, image);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getAllBooks() {
        List<BookResponseDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(ApiResponse.success("Books retrieved successfully", books));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> getBookByIsbn(@PathVariable String isbn) {
        BookResponseDTO book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(ApiResponse.success("Book retrieved successfully", book));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> getBookById(@PathVariable Long id) {
        BookResponseDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponse.success("Book retrieved successfully", book));
    }

    /**
     * Update an existing book. Accepts multipart/form-data with:
     *  - "book": JSON string representing BookRequestDTO
     *  - "image": optional new image file (existing image kept if omitted)
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BookResponseDTO>> updateBook(
            @PathVariable Long id,
            @RequestPart("book") String bookJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        BookRequestDTO requestDTO = objectMapper.readValue(bookJson, BookRequestDTO.class);
        validateManually(requestDTO);

        BookResponseDTO updated = bookService.updateBook(id, requestDTO, image);
        return ResponseEntity.ok(ApiResponse.success("Book updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.success("Book deleted successfully", null));
    }

    /**
     * Upload or replace the cover image for an existing book independently
     * of the main book data update endpoint.
     */
    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BookResponseDTO>> uploadBookImage(
            @PathVariable Long id,
            @RequestPart("image") MultipartFile image) {
        BookResponseDTO updated = bookService.uploadBookImage(id, image);
        return ResponseEntity.ok(ApiResponse.success("Book cover image uploaded successfully", updated));
    }

    /**
     * Manually triggers bean validation on the DTO since @Valid does not
     * automatically apply to a @RequestPart that is deserialized from a JSON string part.
     */
    private void validateManually(BookRequestDTO dto) {
        var validatorFactory = jakarta.validation.Validation.buildDefaultValidatorFactory();
        var validator = validatorFactory.getValidator();
        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            violations.forEach(v -> sb.append(v.getPropertyPath()).append(": ").append(v.getMessage()).append("; "));
            throw new IllegalArgumentException(sb.toString());
        }
    }
}
