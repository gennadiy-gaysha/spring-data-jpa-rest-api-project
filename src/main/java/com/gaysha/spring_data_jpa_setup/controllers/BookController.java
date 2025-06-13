package com.gaysha.spring_data_jpa_setup.controllers;

import com.gaysha.spring_data_jpa_setup.domains.dto.BookDto;
import com.gaysha.spring_data_jpa_setup.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @PutMapping(path = "/books/{isbn}")
    // Retrieving parameters from two different sources
    // - Gets the isbn value from the URL path
    // - Deserializes the JSON request body into a BookDto object
    public ResponseEntity<BookDto> createBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto){
        return new ResponseEntity<>(bookService.createBook(isbn, bookDto), HttpStatus.CREATED);
    }
}
