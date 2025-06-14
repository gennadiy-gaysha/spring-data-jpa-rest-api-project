package com.gaysha.spring_data_jpa_setup.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaysha.spring_data_jpa_setup.TestDataUtil;
import com.gaysha.spring_data_jpa_setup.domains.dto.BookDto;
import com.gaysha.spring_data_jpa_setup.domains.entities.AuthorEntity;
import com.gaysha.spring_data_jpa_setup.domains.entities.BookEntity;
import com.gaysha.spring_data_jpa_setup.repositories.AuthorRepository;
import com.gaysha.spring_data_jpa_setup.repositories.BookRepository;
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

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// Ensures the Spring context is reset after each test method.
// Prevents side effects between tests (like lingering data in the DB).
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// Enables MockMvc to test Spring MVC controllers without starting a full HTTP server.
// Simulates HTTP requests and verifies the responses.
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookControllerIntegrationTests(
            MockMvc mockMvc,
            AuthorRepository authorRepository,
            BookRepository bookRepository
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsHttp201Created() throws Exception{
        // Arrange: Create and persist an author
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        // Save the author to the DB to generate and assign an ID
        authorEntity = authorRepository.save(authorEntity);

        // Arrange: Build a BookDto (no ISBN in the body — comes from the path)
        BookDto bookDto = BookDto.builder()
                .title("Norwegian Wood")
                .authorId(authorEntity.getId())
                .build();

        // Convert the BookDto to a JSON string for the request body
        String bookJson = objectMapper.writeValueAsString(bookDto);

        // Act & Assert: Perform PUT request with ISBN in path
        // Validating the JSON response body returned by the controller
        mockMvc.perform(MockMvcRequestBuilders.put("/books/9780099458326")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                // Expect a 201 Created response after successful POST
                .andExpect(MockMvcResultMatchers.status().isCreated())
                // Verify the returned JSON contains the correct ISBN
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn")
                        .value("9780099458326"))
                // Verify the returned JSON contains the correct title
                // Look inside the JSON response body and extract the value at the field named "title"
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        // Asserts that the value of the "title" field is exactly equal to "Norwegian Wood"
                        .value("Norwegian Wood"))
                // Verify the returned JSON contains the correct author ID
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId")
                        .value(authorEntity.getId().intValue()));
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        BookEntity bookEntityA = TestDataUtil.createTestBookA(authorEntity);
        BookEntity bookEntityB = TestDataUtil.createTestBookB(authorEntity);

        bookRepository.saveAll(List.of(bookEntityA, bookEntityB));
        authorRepository.save(authorEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()")
                        .value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isbn")
                        .value("123-45-678"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title")
                        .value("The Shadow in the Attic"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].authorId")
                        .value(1));

    }
}
