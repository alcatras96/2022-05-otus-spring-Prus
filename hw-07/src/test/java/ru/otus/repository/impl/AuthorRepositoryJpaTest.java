package ru.otus.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.model.Author;
import ru.otus.repository.api.AuthorRepository;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Should insert author.")
    @Test
    void shouldInsertAuthor() {
        Author authorToInsert = new Author("Maxim Maximovich Maximov");

        authorRepository.save(authorToInsert);
        testEntityManager.clear();

        assertThat(authorRepository.findById(authorToInsert.getId()))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(authorToInsert);
    }

    @DisplayName("Should return expected authors list.")
    @Test
    void shouldReturnExpectedAuthorsList() {
        assertThat(authorRepository.findAll())
                .usingRecursiveComparison()
                .isEqualTo(getExpectedAuthorsList());
    }

    @DisplayName("Should return expected author by id.")
    @Test
    void shouldReturnExpectedAuthorById() {
        assertThat(authorRepository.findById(1L))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(new Author(1L, "Ivan Ivanovich Ivanov"));
    }

    @DisplayName("Should update author properly.")
    @Test
    void shouldUpdateAuthorProperly() {
        Author updatedAuthor = new Author(1L, "Ivan Ivanovich Alexandrov");

        authorRepository.save(updatedAuthor);
        testEntityManager.flush();
        testEntityManager.clear();

        assertThat(authorRepository.findById(1L))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(updatedAuthor);
    }

    @DisplayName("Should delete author by id.")
    @Test
    void shouldDeleteAuthorById() {
        Long id = 1L;

        assertThat(authorRepository.findById(id)).isPresent();
        authorRepository.deleteById(id);
        testEntityManager.flush();
        assertThat(authorRepository.findById(id)).isNotPresent();
    }

    private List<Author> getExpectedAuthorsList() {
        return of(new Author(1L, "Ivan Ivanovich Ivanov"), new Author(2L, "Alexander Alexandrovich Alexandrov"));
    }
}