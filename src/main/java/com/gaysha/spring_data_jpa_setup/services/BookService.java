package com.gaysha.spring_data_jpa_setup.services;

import com.gaysha.spring_data_jpa_setup.domains.dto.BookDto;
import com.gaysha.spring_data_jpa_setup.domains.entities.AuthorEntity;
import com.gaysha.spring_data_jpa_setup.domains.entities.BookEntity;
import com.gaysha.spring_data_jpa_setup.mapper.Mapper;
import com.gaysha.spring_data_jpa_setup.repositories.AuthorRepository;
import com.gaysha.spring_data_jpa_setup.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public BookDto createBook(String isbn, BookDto bookDto) {
        // Look up the author in the DB using the ID provided in the bookDto
        AuthorEntity authorEntity = authorRepository
                .findById(bookDto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));
        // Set the ISBN from the path variable into the bookDto
        bookDto.setIsbn(isbn);
        // Convert BookDto to BookEntity
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        // Associate the retrieved AuthorEntity with the BookEntity
        bookEntity.setAuthorEntity(authorEntity);

        // Save the BookEntity to the DB
        bookEntity = bookRepository.save(bookEntity);

        // Convert the saved BookEntity back to BookDto and return it
        return bookMapper.mapTo(bookEntity);
    }

    public List<BookDto> findAllBooks() {
        List<BookEntity> bookEntities = bookRepository.findAll();
        return bookEntities.stream()
                .map(bookMapper::mapTo)
                .collect(Collectors.toList());
    }
}
