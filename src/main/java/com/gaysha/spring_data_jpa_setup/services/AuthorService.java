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
        AuthorEntity author = AuthorEntity.builder()
                .name(authorDto.getName())
                .age(authorDto.getAge())
                .build();

        System.out.println(author.toString());
        // Save Entity
        author = authorRepository.save(author);
        System.out.println(author.toString());

        // Convert Entity back to DTO
        return AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .age(author.getAge())
                .build();
    }
}
