package com.gaysha.spring_data_jpa_setup.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaysha.spring_data_jpa_setup.TestDataUtil;
import com.gaysha.spring_data_jpa_setup.domains.dto.AuthorDto;
import com.gaysha.spring_data_jpa_setup.domains.entities.AuthorEntity;
import com.gaysha.spring_data_jpa_setup.repositories.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

// Loads the full Spring context. Useful for integration testing.
// Application starts like in real life, but without a web server.
@SpringBootTest
@ExtendWith(SpringExtension.class)
// Ensures the Spring context is reset after each test method.
// Prevents side effects between tests (like lingering data in the DB).
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// MockMvc allows to test Spring MVC controllers without starting a
// full HTTP server. Simulates HTTP requests and checks the responses
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests {
    // Allows to simulate HTTP requests to the controllers
    private final MockMvc mockMvc;
    // Converts Java objects ⇄ JSON
    // ObjectMapper is a class provided by Jackson, it's the main class we use to:
    // Serialize Java objects to JSON
    // Deserialize JSON to Java objects
    private final ObjectMapper objectMapper;
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, AuthorRepository authorRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.authorRepository = authorRepository;
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorDto testAuthorDto = AuthorDto.builder()
                .name("George Orwell")
                .age(46)
                .build();
        // We're simulating what a real HTTP client (like Postman or a frontend app)
        // would send:
        // - testAuthorDto is a Java object.
        // - objectMapper.writeValueAsString(...) converts it to a JSON string.
        // We then send that JSON string as the body of the HTTP request via MockMvc
        // In production Spring + Jackson auto handles JSON - Java Object conversion
        String authorJson = objectMapper.writeValueAsString(testAuthorDto);

        // Performs an HTTP POST request to /authors with the JSON as the request body:
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/authors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))
                .andExpect(MockMvcResultMatchers.status().isCreated()
                );
    }

    // This test checks the entire flow, which includes:
    // - Controller layer (AuthorController)
    // - Service layer (AuthorService.createAuthor(...))
    // - Repository layer (AuthorRepository.save(...))
    // - DTO ↔ Entity mapping (if applicable)
    // Integration tests simulate real HTTP requests and
    // go through the full application context
    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorDto testAuthorDto = AuthorDto.builder()
                .name("George Orwell")
                .age(46)
                .build();
        String authorJson = objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.status().isCreated()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.id")
                                .isNumber()
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name")
                                .value("George Orwell")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.age")
                                .value(46)
                );
    }

    @Test
    public void testThatFindAllAuthorsReturnsListOfAuthors() throws Exception {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorB();

        authorRepository.save(authorEntityA);
        authorRepository.save(authorEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()")
                        .value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name")
                        .value("Abigail Rose"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age")
                        .value(80))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id")
                        .value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name")
                        .value("Jane Austen"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age")
                        .value(41));
    }

    @Test
    public void testThatGetOneAuthorsReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        authorRepository.save(authorEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetOneAuthorsReturnsHttpStatus404WhenAuthorDoesNotExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void testThatGetOneAuthorsReturnsAuthorWhenAuthorExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        authorRepository.save(authorEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("Abigail Rose"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age")
                        .value(80));
    }

}
