package com.gaysha.spring_data_jpa_setup;

import com.gaysha.spring_data_jpa_setup.domains.Author;
import com.gaysha.spring_data_jpa_setup.domains.Book;

public final class TestDataUtil {
    public TestDataUtil() {
    }

    public static Author createTestAuthorA() {
        return Author.builder()
                // .id(1L)
                .name("Abigail Rose")
                .age(80)
                .build();
    }

    public static Author createTestAuthorB() {
        return Author.builder()
                // .id(2L)
                .name("Jane Austen")
                .age(41)
                .build();
    }

    public static Author createTestAuthorC() {
        return Author.builder()
                // .id(3L)
                .name("George Orwell")
                .age(46)
                .build();
    }

    public static Book createTestBookA(final Author author) {
        return Book.builder()
                .isbn("123-45-678")
                .title("The Shadow in the Attic")
                .author(author)
                .build();
    }

    public static Book createTestBookB(final Author author) {
        return Book.builder()
                .isbn("123-45-678-0")
                .title("Pride and Prejudice")
                .author(author)
                .build();
    }

    public static Book createTestBookC(final Author author) {
        return Book.builder()
                .isbn("123-45-678-1")
                .title("1984")
                .author(author)
                .build();
    }
}
