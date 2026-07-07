package com.bookapi.bookmanagement.service;

import com.bookapi.bookmanagement.dto.BookRequestDTO;
import com.bookapi.bookmanagement.dto.BookResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service layer contract for Book business logic.
 */
public interface BookService {

    BookResponseDTO createBook(BookRequestDTO requestDTO, MultipartFile coverImage);

    List<BookResponseDTO> getAllBooks();

    BookResponseDTO getBookByIsbn(String isbn);

    BookResponseDTO getBookById(Long id);

    BookResponseDTO updateBook(Long id, BookRequestDTO requestDTO, MultipartFile coverImage);

    void deleteBook(Long id);

    BookResponseDTO uploadBookImage(Long id, MultipartFile coverImage);
}
