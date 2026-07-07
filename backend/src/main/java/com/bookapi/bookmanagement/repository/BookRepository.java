package com.bookapi.bookmanagement.repository;

import com.bookapi.bookmanagement.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for performing CRUD operations on Book entities.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    void deleteByIsbn(String isbn);
}
