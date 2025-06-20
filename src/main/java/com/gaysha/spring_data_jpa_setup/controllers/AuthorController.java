package com.gaysha.spring_data_jpa_setup.controllers;

import com.gaysha.spring_data_jpa_setup.domains.dto.AuthorDto;
import com.gaysha.spring_data_jpa_setup.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping(path = "/authors")
    // ResponseEntity<AuthorDto> is a wrapper for the HTTP response that:
    // - Sends back data — here it's the AuthorDto (the created author).
    // - Sets a specific HTTP status — in this case, 201 CREATED.
    // Under the hood:
    // - Spring Boot receives the HTTP request (JSON).
    // - It sees @RequestBody, so it knows the body should be mapped to a Java object.
    // - It uses a registered HttpMessageConverter — in most Spring Boot apps,
    // this is Jackson's MappingJackson2HttpMessageConverter.
    // - Jackson automatically deserializes the JSON into an AuthorDto before the
    // method is called.
    // - Injects authorDto parameter in the method
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto){
        return new ResponseEntity<>(authorService.createAuthor(authorDto), HttpStatus.CREATED);
    }

    // 1. A client (like a browser or frontend app) sends a GET request to "/authors".
    @GetMapping(path = "/authors")
    // 2. The method findAll() is triggered to get all authors from the database.
    public ResponseEntity<List<AuthorDto>> findAllAuthors(){
        List<AuthorDto> authorDtos = authorService.findAllAuthors();
        // .ok() is a factory method that creates a ResponseEntity with HTTP status 200 OK
        // and a body containing authorDtos,
        // which will be automatically converted to JSON by Spring (using Jackson).

        // The result (List<AuthorDto>) is returned as a JSON response with HTTP status 200 OK.
        return ResponseEntity.ok(authorDtos);
    }

    // Controller handles incoming HTTP GET requests
    @GetMapping(path = "/authors/{id}")
    ResponseEntity<AuthorDto> getOneAuthor(@PathVariable("id") Long id){
        // Takes Optional from AuthorService and automatically:
        // Returns 200 OK with the body if the value is present.
        // Returns 404 Not Found if the Optional is empty.
        return ResponseEntity.of(authorService.getOneAuthor(id));

        // The line above substitutes this code block:
        /*Optional<AuthorDto> optionalAuthorDto = authorService.getOneAuthor(id);
        return optionalAuthorDto
                .map(authorDto -> new ResponseEntity<>(authorDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));*/
    }

    // Full Author update:
    // any missing fields are overwritten with null (or defaults)
    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(
            @PathVariable("id") Long id,
            @RequestBody AuthorDto authorDto){
        return ResponseEntity.of(authorService.updateAuthor(id, authorDto));
    }

    // Partial Author update:
    // @RequestBody contains only the fields to be updated
    // other fields stay untouched

    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> patchAuthor(
            @PathVariable("id") Long id,
            @RequestBody AuthorDto authorDto){
        return ResponseEntity.of(authorService.patchAuthor(id, authorDto));
    }
}
