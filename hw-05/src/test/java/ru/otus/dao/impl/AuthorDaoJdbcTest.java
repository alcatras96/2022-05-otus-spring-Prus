package ru.otus.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import ru.otus.domain.Author;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {

    @Autowired
    private AuthorDaoJdbc authorDao;

    @DisplayName("Should insert author.")
    @Test
    void shouldInsertAuthor() {
        Author authorToInsert = new Author("Maxim Maximovich Maximov");

        Long bookId = authorDao.insert(authorToInsert);
        authorToInsert.setId(bookId);

        assertEquals(authorToInsert, authorDao.getById(bookId));
    }

    @DisplayName("Should return expected authors list.")
    @Test
    void shouldReturnExpectedAuthorsList() {
        assertEquals(getExpectedAuthorsList(), authorDao.getAll());
    }

    @DisplayName("Should return expected author by id.")
    @Test
    void shouldReturnExpectedAuthorById() {
        assertEquals(new Author(1L, "Ivan Ivanovich Ivanov"), authorDao.getById(1L));
    }

    @DisplayName("Should update author properly.")
    @Test
    void shouldUpdateAuthorProperly() {
        Author updatedAuthor = new Author(1L, "Ivan Ivanovich Alexandrov");

        authorDao.update(updatedAuthor);

        assertEquals(updatedAuthor, authorDao.getById(1L));
    }

    @DisplayName("Should update author full name.")
    @Test
    void shouldUpdateAuthorFullName() {
        String updatedFullName = "Ivan Ivanovich Alexandrov";
        Long id = 1L;

        authorDao.updateFullNameById(id, updatedFullName);
        assertEquals(new Author(id, updatedFullName), authorDao.getById(id));
    }

    @DisplayName("Should delete author by id.")
    @Test
    void shouldDeleteAuthorById() {
        Long id = 1L;
        authorDao.deleteById(id);

        assertThatThrownBy(() -> authorDao.getById(id)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("Should throw an exception if update non-existent author.")
    @Test
    void shouldThrowExceptionIfUpdateNonExistentAuthor() {
        assertThatThrownBy(() -> authorDao.update(new Author(Long.MAX_VALUE, "name")))
                .isInstanceOf(JdbcUpdateAffectedIncorrectNumberOfRowsException.class);
    }

    private List<Author> getExpectedAuthorsList() {
        return of(new Author(1L, "Ivan Ivanovich Ivanov"), new Author(2L, "Alexander Alexandrovich Alexandrov"));
    }
}