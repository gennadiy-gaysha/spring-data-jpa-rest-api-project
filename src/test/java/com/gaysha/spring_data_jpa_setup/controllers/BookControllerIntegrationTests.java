package com.gaysha.spring_data_jpa_setup.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaysha.spring_data_jpa_setup.TestDataUtil;
import com.gaysha.spring_data_jpa_setup.domains.dto.BookDto;
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

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, AuthorRepository authorRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.authorRepository = authorRepository;
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsHttp201Created() throws Exception{
        // Create an AuthorEntity instance using test data
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        // Save the author to the DB to generate and assign an ID
        authorEntity = authorRepository.save(authorEntity);

        // Create a BookDto without an authorId (null)
        BookDto bookDto = BookDto.builder()
                .isbn("9780099458326")
                .title("Norwegian Wood")
                .build();

        // Associate the book with the saved author
        bookDto.setAuthorId(authorEntity.getId());

        // Convert the BookDto to a JSON string for the request body
        String bookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                // Expect a 201 Created response after successful POST
                .andExpect(MockMvcResultMatchers.status().isCreated())
                // Verify the returned JSON contains the correct ISBN
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn")
                        .value("9780099458326"))
                // Verify the returned JSON contains the correct title
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Norwegian Wood"))
                // Verify the returned JSON contains the correct author ID
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId")
                        .value(authorEntity.getId().intValue()));
    }
}
