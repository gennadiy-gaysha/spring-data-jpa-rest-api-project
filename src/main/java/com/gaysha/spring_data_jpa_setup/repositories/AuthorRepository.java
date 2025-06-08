package com.gaysha.spring_data_jpa_setup.repositories;

import com.gaysha.spring_data_jpa_setup.domains.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    List<AuthorEntity> ageLessThan(int age);

    @Query("SELECT a FROM AuthorEntity a WHERE a.age < :age1  AND a.age > :age2")
    List<AuthorEntity> findAgeBetween(int age1, int age2);

    @Query("SELECT a FROM AuthorEntity a WHERE a.age > :age")
    List<AuthorEntity> findAgeGreaterThan(int age);
}
