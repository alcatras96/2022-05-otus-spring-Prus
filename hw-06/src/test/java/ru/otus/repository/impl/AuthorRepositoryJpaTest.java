package ru.otus.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.model.Author;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AuthorRepositoryJpa.class)
class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepositoryJpa authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("Should insert author.")
    @Test
    void shouldInsertAuthor() {
        Author authorToInsert = new Author("Maxim Maximovich Maximov");

        authorRepository.insert(authorToInsert);
        entityManager.clear();

        assertThat(authorRepository.getById(authorToInsert.getId()))
                .usingRecursiveComparison()
                .isEqualTo(authorToInsert);
    }

    @DisplayName("Should return expected authors list.")
    @Test
    void shouldReturnExpectedAuthorsList() {
        assertThat(authorRepository.getAll())
                .usingRecursiveComparison()
                .isEqualTo(getExpectedAuthorsList());
    }

    @DisplayName("Should return expected author by id.")
    @Test
    void shouldReturnExpectedAuthorById() {
        assertThat(authorRepository.getById(1L))
                .usingRecursiveComparison()
                .isEqualTo(new Author(1L, "Ivan Ivanovich Ivanov"));
    }

    @DisplayName("Should update author properly.")
    @Test
    void shouldUpdateAuthorProperly() {
        Author updatedAuthor = new Author(1L, "Ivan Ivanovich Alexandrov");

        authorRepository.update(updatedAuthor);
        entityManager.flush();

        assertThat(updatedAuthor)
                .usingRecursiveComparison()
                .isEqualTo(authorRepository.getById(1L));
    }

    @DisplayName("Should update author full name.")
    @Test
    void shouldUpdateAuthorFullName() {
        String updatedFullName = "Ivan Ivanovich Alexandrov";
        Long id = 1L;

        authorRepository.updateFullNameById(id, updatedFullName);
        entityManager.clear();

        assertThat(authorRepository.getById(id))
                .usingRecursiveComparison()
                .isEqualTo(new Author(id, updatedFullName));
    }

    @DisplayName("Should delete author by id.")
    @Test
    void shouldDeleteAuthorById() {
        Long id = 1L;
        assertThat(authorRepository.getById(id)).isNotNull();
        authorRepository.deleteById(id);
        entityManager.clear();
        assertThat(authorRepository.getById(id)).isNull();
    }

    private List<Author> getExpectedAuthorsList() {
        return of(new Author(1L, "Ivan Ivanovich Ivanov"), new Author(2L, "Alexander Alexandrovich Alexandrov"));
    }
}