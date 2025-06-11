package com.gaysha.spring_data_jpa_setup.services;

import com.gaysha.spring_data_jpa_setup.domains.dto.BookDto;
import com.gaysha.spring_data_jpa_setup.domains.entities.AuthorEntity;
import com.gaysha.spring_data_jpa_setup.domains.entities.BookEntity;
import com.gaysha.spring_data_jpa_setup.mapper.Mapper;
import com.gaysha.spring_data_jpa_setup.repositories.AuthorRepository;
import com.gaysha.spring_data_jpa_setup.repositories.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final Mapper<BookEntity, BookDto> bookMapper;
    private final AuthorRepository authorRepository;

    public BookService(
            BookRepository bookRepository,
            Mapper<BookEntity, BookDto> bookMapper,
            AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorRepository = authorRepository;
    }

    public BookDto createBook(BookDto bookDto) {
        AuthorEntity authorEntity = authorRepository
                .findById(bookDto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        bookEntity.setAuthorEntity(authorEntity);

        bookEntity = bookRepository.save(bookEntity);

        return bookMapper.mapTo(bookEntity);
    }
}
