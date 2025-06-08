package com.gaysha.spring_data_jpa_setup;

import com.gaysha.spring_data_jpa_setup.domains.entities.AuthorEntity;
import com.gaysha.spring_data_jpa_setup.domains.entities.BookEntity;

public final class TestDataUtil {
    public TestDataUtil() {
    }

    public static AuthorEntity createTestAuthorA() {
        return AuthorEntity.builder()
                // .id(1L)
                .name("Abigail Rose")
                .age(80)
                .build();
    }

    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
                // .id(2L)
                .name("Jane Austen")
                .age(41)
                .build();
    }

    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder()
                // .id(3L)
                .name("George Orwell")
                .age(46)
                .build();
    }

    public static BookEntity createTestBookA(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("123-45-678")
                .title("The Shadow in the Attic")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookEntity createTestBookB(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("123-45-678-0")
                .title("Pride and Prejudice")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookEntity createTestBookC(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("123-45-678-1")
                .title("1984")
                .authorEntity(authorEntity)
                .build();
    }
}
