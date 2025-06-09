package com.gaysha.spring_data_jpa_setup.mapper.impl;

import com.gaysha.spring_data_jpa_setup.domains.dto.AuthorDto;
import com.gaysha.spring_data_jpa_setup.domains.entities.AuthorEntity;
import com.gaysha.spring_data_jpa_setup.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapperImpl implements Mapper<AuthorEntity, AuthorDto> {
    public final ModelMapper modelMapper;

    public AuthorMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AuthorEntity mapFrom(AuthorDto authorDto) {
        return modelMapper.map(authorDto, AuthorEntity.class);
    }

    @Override
    public AuthorDto mapTo(AuthorEntity authorEntity) {
        return modelMapper.map(authorEntity, AuthorDto.class);
    }
}
