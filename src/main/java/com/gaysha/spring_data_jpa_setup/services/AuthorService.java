package com.gaysha.spring_data_jpa_setup.services;

import com.gaysha.spring_data_jpa_setup.domains.dto.AuthorDto;
import com.gaysha.spring_data_jpa_setup.domains.entities.AuthorEntity;
import com.gaysha.spring_data_jpa_setup.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorDto createAuthor(AuthorDto authorDto) {
        // Convert DTO to Entity
        AuthorEntity author = new AuthorEntity();
        author = AuthorEntity.builder()
                .name(authorDto.getName())
                .age(authorDto.getAge())
                .build();


        // Save Entity
        AuthorEntity savedAuthor = authorRepository.save(author);

        // Convert Entity back to DTO
        return AuthorDto.builder()
                .id(savedAuthor.getId())
                .name(savedAuthor.getName())
                .age(savedAuthor.getAge())
                .build();
    }
}
