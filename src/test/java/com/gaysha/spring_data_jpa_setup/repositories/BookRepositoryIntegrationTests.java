package com.gaysha.spring_data_jpa_setup.repositories;

import com.gaysha.spring_data_jpa_setup.TestDataUtil;
import com.gaysha.spring_data_jpa_setup.domains.Author;
import com.gaysha.spring_data_jpa_setup.domains.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookRepositoryIntegrationTests {
    private final BookRepository underTest;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookRepositoryIntegrationTests(BookRepository underTest, AuthorRepository authorRepository ) {
        this.underTest = underTest;
        this.authorRepository = authorRepository;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {
        Author author = TestDataUtil.createTestAuthorA();

        Book book = TestDataUtil.createTestBookA(author);

        underTest.save(book);

        Optional<Book> result = underTest.findById(book.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled() {
        Author author = TestDataUtil.createTestAuthorA();
        // When we call the line below, it does two things:
        // - Saves the Author to the database (i.e., performs an INSERT or UPDATE).
        // - Returns the same Author entity — but now with any generated values filled
        // in (like the auto-generated id).
        // This is the standard behavior of Spring Data JPA’s save() method: it returns
        // a managed, updated version of the entity. You can and should use it when you
        // need the database-generated fields like @Id
        Author savedAuthor = authorRepository.save(author);

        Book bookA = TestDataUtil.createTestBookA(savedAuthor);
        underTest.save(bookA);
        Book bookB = TestDataUtil.createTestBookB(savedAuthor);
        underTest.save(bookB);
        Book bookC = TestDataUtil.createTestBookC(savedAuthor);
        underTest.save(bookC);

        List<Book> result = underTest.findAll();
        System.out.println(result);
        assertThat(result)
                .hasSize(3)
                .containsExactly(bookA, bookB, bookC);
    }

    @Test
    public void testThatBookCanBeUpdated() {
        // SETUP
        Author author = TestDataUtil.createTestAuthorA();
        Author savedAuthor = authorRepository.save(author);

        Book book = TestDataUtil.createTestBookA(savedAuthor);
        underTest.save(book);

        // EXECUTE
        book.setTitle("Updated");
        underTest.save(book);
        System.out.println(book);

        // ASSERT
        Optional<Book> result = underTest.findById(book.getIsbn());
        assertThat(result).isPresent();
        System.out.println(result.get());
        assertThat(result.get()).isEqualTo(book);
    }

    @Test
    public void testThatBookCanBeDeleted() {
        Author author = TestDataUtil.createTestAuthorA();
        System.out.println(author);
        Author savedAuthor = authorRepository.save(author);
        System.out.println(savedAuthor);

        Book book = TestDataUtil.createTestBookA(savedAuthor);
        underTest.save(book);

        Optional<Book> beforeDelete = underTest.findById(book.getIsbn());
        underTest.deleteById(book.getIsbn());
        Optional<Book> afterDelete = underTest.findById(book.getIsbn());

        assertThat(beforeDelete).isPresent();
        assertThat(afterDelete).isNotPresent();
    }
}


