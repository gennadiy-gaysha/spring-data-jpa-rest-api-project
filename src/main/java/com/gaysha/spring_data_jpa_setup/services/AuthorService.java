package com.gaysha.spring_data_jpa_setup.services;

import com.gaysha.spring_data_jpa_setup.domains.dto.AuthorDto;
import com.gaysha.spring_data_jpa_setup.domains.entities.AuthorEntity;
import com.gaysha.spring_data_jpa_setup.mapper.Mapper;
import com.gaysha.spring_data_jpa_setup.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final Mapper<AuthorEntity, AuthorDto> authorMapper;

    public AuthorService(
            AuthorRepository authorRepository,
            Mapper<AuthorEntity, AuthorDto> authorMapper
    ) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    // Create method to be called during POST request
    public AuthorDto createAuthor(AuthorDto authorDto) {
        // Convert DTO to Entity
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);

        // Save Entity
        authorEntity = authorRepository.save(authorEntity);

        // Convert Entity back to DTO
        return authorMapper.mapTo(authorEntity);
    }

    // Create method to be called during GET request to "/authors"
    public List<AuthorDto> findAllAuthors() {
        // 1. AuthorService findAll() method creates a list of AuthorEntity objects
        // (which represent rows from the database) to convert them into a list of
        // AuthorDto objects (which are lighter, safer versions used in your API)
        List<AuthorEntity> authorEntities = authorRepository.findAll();

        // 2. Turn list of authorEntities into a stream (concise alternative to the
        // enhanced for loop).
        return authorEntities.stream()
                // 3. Transforms each AuthorEntity into an AuthorDto.
                // map(...) means: “Take each item in the stream and apply this function to it.”
                // authorMapper::mapTo is a method reference — a short way of:
                // map(authorEntity -> authorMapper.mapTo(authorEntity))
                .map(authorMapper::mapTo)
                // 4. Ends the stream operation and produces the final result: a list of DTOs.
                .collect(Collectors.toList());
    }
}
