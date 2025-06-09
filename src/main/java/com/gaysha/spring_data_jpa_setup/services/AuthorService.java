package com.gaysha.spring_data_jpa_setup.services;

import com.gaysha.spring_data_jpa_setup.domains.dto.AuthorDto;
import com.gaysha.spring_data_jpa_setup.domains.entities.AuthorEntity;
import com.gaysha.spring_data_jpa_setup.mapper.Mapper;
import com.gaysha.spring_data_jpa_setup.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

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

    public AuthorDto createAuthor(AuthorDto authorDto) {
        // Convert DTO to Entity
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);

        // Save Entity
        authorEntity = authorRepository.save(authorEntity);

        // Convert Entity back to DTO
        return authorMapper.mapTo(authorEntity);
    }
}
