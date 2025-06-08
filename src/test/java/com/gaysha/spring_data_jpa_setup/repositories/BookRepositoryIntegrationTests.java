package com.gaysha.spring_data_jpa_setup.repositories;

import com.gaysha.spring_data_jpa_setup.TestDataUtil;
import com.gaysha.spring_data_jpa_setup.domains.entities.AuthorEntity;
import com.gaysha.spring_data_jpa_setup.domains.entities.BookEntity;
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
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();

        BookEntity bookEntity = TestDataUtil.createTestBookA(authorEntity);

        underTest.save(bookEntity);

        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get().getIsbn()).isEqualTo(bookEntity.getIsbn());
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        // When we call the line below, it does two things:
        // - Saves the Author to the database (i.e., performs an INSERT or UPDATE).
        // - Returns the same Author entity — but now with any generated values filled
        // in (like the auto-generated id).
        // This is the standard behavior of Spring Data JPA’s save() method: it returns
        // a managed, updated version of the entity. You can and should use it when you
        // need the database-generated fields like @Id
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);

        BookEntity bookEntityA = TestDataUtil.createTestBookA(savedAuthorEntity);
        underTest.save(bookEntityA);
        BookEntity bookEntityB = TestDataUtil.createTestBookB(savedAuthorEntity);
        underTest.save(bookEntityB);
        BookEntity bookEntityC = TestDataUtil.createTestBookC(savedAuthorEntity);
        underTest.save(bookEntityC);

        List<BookEntity> result = underTest.findAll();
        System.out.println(result);
        assertThat(result)
                .hasSize(3)
                .containsExactly(bookEntityA, bookEntityB, bookEntityC);
    }

    @Test
    public void testThatBookCanBeUpdated() {
        // SETUP
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);

        BookEntity bookEntity = TestDataUtil.createTestBookA(savedAuthorEntity);
        underTest.save(bookEntity);

        // EXECUTE
        bookEntity.setTitle("Updated");
        underTest.save(bookEntity);
        System.out.println(bookEntity);

        // ASSERT
        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isPresent();
        System.out.println(result.get());
        assertThat(result.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatBookCanBeDeleted() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        System.out.println(authorEntity);
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);
        System.out.println(savedAuthorEntity);

        BookEntity bookEntity = TestDataUtil.createTestBookA(savedAuthorEntity);
        underTest.save(bookEntity);

        Optional<BookEntity> beforeDelete = underTest.findById(bookEntity.getIsbn());
        underTest.deleteById(bookEntity.getIsbn());
        Optional<BookEntity> afterDelete = underTest.findById(bookEntity.getIsbn());

        assertThat(beforeDelete).isPresent();
        assertThat(afterDelete).isNotPresent();
    }
}


