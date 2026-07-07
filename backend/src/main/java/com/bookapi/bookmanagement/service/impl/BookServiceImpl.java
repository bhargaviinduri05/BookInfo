package com.bookapi.bookmanagement.service.impl;

import com.bookapi.bookmanagement.dto.BookRequestDTO;
import com.bookapi.bookmanagement.dto.BookResponseDTO;
import com.bookapi.bookmanagement.entity.Book;
import com.bookapi.bookmanagement.exception.DuplicateResourceException;
import com.bookapi.bookmanagement.exception.ResourceNotFoundException;
import com.bookapi.bookmanagement.repository.BookRepository;
import com.bookapi.bookmanagement.service.BookService;
import com.bookapi.bookmanagement.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Implementation of BookService containing core business logic for
 * managing books, including delegation to FileStorageService for
 * cover image handling.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public BookResponseDTO createBook(BookRequestDTO requestDTO, MultipartFile coverImage) {
        if (bookRepository.existsByIsbn(requestDTO.getIsbn())) {
            throw new DuplicateResourceException(
                    "A book with ISBN " + requestDTO.getIsbn() + " already exists");
        }

        Book book = mapToEntity(requestDTO);

        if (coverImage != null && !coverImage.isEmpty()) {
            String storedFilename = fileStorageService.storeFile(coverImage);
            book.setImagePath(storedFilename);
        }

        Book saved = bookRepository.save(book);
        log.info("Created book with id={} isbn={}", saved.getId(), saved.getIsbn());
        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDTO getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ISBN: " + isbn));
        return mapToResponseDTO(book);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return mapToResponseDTO(book);
    }

    @Override
    @Transactional
    public BookResponseDTO updateBook(Long id, BookRequestDTO requestDTO, MultipartFile coverImage) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        // If ISBN is being changed, make sure the new ISBN isn't already taken by another book
        if (!existing.getIsbn().equals(requestDTO.getIsbn())
                && bookRepository.existsByIsbn(requestDTO.getIsbn())) {
            throw new DuplicateResourceException(
                    "A book with ISBN " + requestDTO.getIsbn() + " already exists");
        }

        existing.setTitle(requestDTO.getTitle());
        existing.setAuthor(requestDTO.getAuthor());
        existing.setIsbn(requestDTO.getIsbn());
        existing.setPublisher(requestDTO.getPublisher());
        existing.setGenre(requestDTO.getGenre());
        existing.setPrice(requestDTO.getPrice());
        existing.setQuantity(requestDTO.getQuantity());
        existing.setDescription(requestDTO.getDescription());

        if (coverImage != null && !coverImage.isEmpty()) {
            String oldImage = existing.getImagePath();
            String storedFilename = fileStorageService.storeFile(coverImage);
            existing.setImagePath(storedFilename);
            if (StringUtils.hasText(oldImage)) {
                fileStorageService.deleteFile(oldImage);
            }
        }

        Book updated = bookRepository.save(existing);
        log.info("Updated book with id={}", updated.getId());
        return mapToResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        if (StringUtils.hasText(existing.getImagePath())) {
            fileStorageService.deleteFile(existing.getImagePath());
        }

        bookRepository.delete(existing);
        log.info("Deleted book with id={}", id);
    }

    @Override
    @Transactional
    public BookResponseDTO uploadBookImage(Long id, MultipartFile coverImage) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        String oldImage = existing.getImagePath();
        String storedFilename = fileStorageService.storeFile(coverImage);
        existing.setImagePath(storedFilename);

        Book updated = bookRepository.save(existing);

        if (StringUtils.hasText(oldImage)) {
            fileStorageService.deleteFile(oldImage);
        }

        log.info("Uploaded cover image for book id={}", id);
        return mapToResponseDTO(updated);
    }

    // ---------------------------------------------------------------------
    // Mapping helpers
    // ---------------------------------------------------------------------

    private Book mapToEntity(BookRequestDTO dto) {
        return Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .publisher(dto.getPublisher())
                .genre(dto.getGenre())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .description(dto.getDescription())
                .build();
    }

    private BookResponseDTO mapToResponseDTO(Book book) {
        return BookResponseDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .genre(book.getGenre())
                .price(book.getPrice())
                .quantity(book.getQuantity())
                .description(book.getDescription())
                .imageUrl(fileStorageService.buildFileUrl(book.getImagePath()))
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}
