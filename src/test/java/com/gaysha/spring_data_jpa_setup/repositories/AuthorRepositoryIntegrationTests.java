package com.gaysha.spring_data_jpa_setup.repositories;

import com.gaysha.spring_data_jpa_setup.TestDataUtil;
import com.gaysha.spring_data_jpa_setup.domains.Author;
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
public class AuthorRepositoryIntegrationTests {
    private final AuthorRepository underTest;

    @Autowired
    public AuthorRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled() {
        Author author = TestDataUtil.createTestAuthorA();
        underTest.save(author);

        Optional<Author> result = underTest.findById(author.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(author);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled() {
        Author authorA = TestDataUtil.createTestAuthorA();
        Author authorB = TestDataUtil.createTestAuthorB();
        Author authorC = TestDataUtil.createTestAuthorC();

        underTest.save(authorA);
        underTest.save(authorB);
        underTest.save(authorC);

        List<Author> result = underTest.findAll();
        System.out.println(result);

        assertThat(result)
                .hasSize(3)
                .containsExactly(authorA, authorB, authorC);
    }

    @Test
    public void testThatAuthorCanBeUpdated() {
        Author author = TestDataUtil.createTestAuthorA();
        underTest.save(author);

        author.setName("Updated");
        System.out.println(author);

        underTest.save(author);
        Optional<Author> result = underTest.findById(author.getId());

        assertThat(result).isPresent();
        System.out.println(result.get());
        assertThat(result.get()).isEqualTo(author);
    }

    @Test
    public void testThatAuthorCanBeDeleted() {
        Author author = TestDataUtil.createTestAuthorA();
        underTest.save(author);

        underTest.deleteById(author.getId());

        Optional<Author> result = underTest.findById(author.getId());
        assertThat(result).isEmpty();
    }

    @Test
    public void testThatGetAuthorsWithAgeLessThan() {
        Author authorA = TestDataUtil.createTestAuthorA();
        underTest.save(authorA);

        Author authorB = TestDataUtil.createTestAuthorB();
        underTest.save(authorB);

        Author authorC = TestDataUtil.createTestAuthorC();
        underTest.save(authorC);

        List<Author> result = underTest.ageLessThan(50);
        System.out.println(result);
        assertThat(result).containsExactly(authorB, authorC);
    }

    @Test
    public void testThatGetAuthorsWithAgeLessThanAndMoreThan() {
        Author authorA = TestDataUtil.createTestAuthorA();
        underTest.save(authorA);

        Author authorB = TestDataUtil.createTestAuthorB();
        underTest.save(authorB);

        Author authorC = TestDataUtil.createTestAuthorC();
        underTest.save(authorC);

        List<Author> result = underTest.findAge(80, 45);
        System.out.println(result);
        assertThat(result).containsExactly(authorC);
    }

    @Test
    public void testThatGetAuthorsWithAgeMoreThan(){
        Author authorA = TestDataUtil.createTestAuthorA();
        underTest.save(authorA);

        Author authorB = TestDataUtil.createTestAuthorB();
        underTest.save(authorB);

        Author authorC = TestDataUtil.createTestAuthorC();
        underTest.save(authorC);

        List<Author> result = underTest.ageShouldBeMoreThan(50);
        System.out.println(result);
        assertThat(result).containsExactly(authorA);

    }
}

// 3:38:17
