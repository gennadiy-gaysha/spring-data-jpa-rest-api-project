package com.gaysha.spring_data_jpa_setup.domains.dto;

import com.gaysha.spring_data_jpa_setup.domains.entities.AuthorEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private String isbn;
    private String title;
    // For incoming requests
    private Long authorId;

    // For outgoing responses
    private AuthorEntity authorEntity;
}
