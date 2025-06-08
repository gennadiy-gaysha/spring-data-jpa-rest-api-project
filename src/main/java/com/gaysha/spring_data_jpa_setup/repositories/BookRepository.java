package com.gaysha.spring_data_jpa_setup.repositories;

import com.gaysha.spring_data_jpa_setup.domains.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, String> {
}
