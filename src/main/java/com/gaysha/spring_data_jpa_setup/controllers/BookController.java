package com.gaysha.spring_data_jpa_setup.controllers;

import com.gaysha.spring_data_jpa_setup.domains.dto.BookDto;
import com.gaysha.spring_data_jpa_setup.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(path = "/books")
    public ResponseEntity<List<BookDto>> findAllBooks(){
        List<BookDto> bookDtos = bookService.findAllBooks();
        return ResponseEntity.ok(bookDtos);
    }
}
