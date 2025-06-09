package com.gaysha.spring_data_jpa_setup.services;

import com.gaysha.spring_data_jpa_setup.domains.dto.BookDto;
import com.gaysha.spring_data_jpa_setup.domains.entities.AuthorEntity;
import com.gaysha.spring_data_jpa_setup.domains.entities.BookEntity;
import com.gaysha.spring_data_jpa_setup.repositories.AuthorRepository;
import com.gaysha.spring_data_jpa_setup.repositories.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public BookDto createBook(BookDto bookDto) {
        AuthorEntity author = authorRepository.findById(bookDto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found!"));

        // Convert DTO to Entity
        BookEntity book = BookEntity
                .builder()
                .isbn(bookDto.getIsbn())
                .title(bookDto.getTitle())
                .authorEntity(author)
                .build();

        // Save Entity
        book = bookRepository.save(book);

        // Convert Entity back to DTO
        return BookDto.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .authorId(book.getAuthorEntity().getId())
                .authorEntity(book.getAuthorEntity())
                .build();
    }
}
