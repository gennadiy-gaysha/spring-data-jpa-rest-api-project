package com.gaysha.spring_data_jpa_setup.controllers;

import com.gaysha.spring_data_jpa_setup.domains.dto.AuthorDto;
import com.gaysha.spring_data_jpa_setup.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
