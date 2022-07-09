package ru.otus.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import ru.otus.dao.api.GenreDao;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@JdbcTest
@Import(BookDaoJdbc.class)
class BookDaoJdbcTest {

    @Autowired
    private BookDaoJdbc bookDao;

    @MockBean
    private GenreDao genreDao;

    @BeforeEach
    void init() {
        when(genreDao.getByIds(any())).thenReturn(emptyList());
        when(genreDao.getAllUsed()).thenReturn(emptyList());
    }

    @DisplayName("Should correctly insert a book with relations.")
    @Test
    void shouldInsertBook() {
        List<Genre> genres = of(new Genre(1L), new Genre(2L));
        Book book = new Book(null, "new book", 1L, genres);

        Long bookId = bookDao.insert(book);
        book.setId(bookId);

        assertThat(book)
                .usingRecursiveComparison()
                .ignoringFields("genres")
                .isEqualTo(bookDao.getById(bookId));

        verify(genreDao, times(1))
                .getByIds(
                        genres.stream()
                                .map(Genre::getId)
                                .collect(toSet())
                );
    }

    @DisplayName("Should throw an exception for incorrect relations when inserting a book.")
    @Test
    void shouldThrowExceptionForIncorrectRelationsWhenInsertingBook() {
        Book book = new Book("new book", Long.MAX_VALUE);
        assertThatThrownBy(() -> bookDao.insert(book)).isInstanceOf(DataIntegrityViolationException.class);

        book.setAuthorId(1L);
        book.setGenres(of(new Genre(Long.MAX_VALUE)));
        assertThatThrownBy(() -> bookDao.insert(book)).isInstanceOf(DataIntegrityViolationException.class);
    }


    @DisplayName("Should return expected books list.")
    @Test
    void shouldReturnExpectedBooksList() {
        assertEquals(getExpectedBooksList(), bookDao.getAll());
    }

    @DisplayName("Should return expected book by id.")
    @Test
    void shouldReturnExpectedBookById() {
        assertEquals(getPulpFictionBook(), bookDao.getById(1L));
    }

    @DisplayName("Should update book properly.")
    @Test
    void shouldUpdateBookProperly() {
        Book bookToUpdate = getPulpFictionBook();

        bookToUpdate.setName("Pulp fiction v2");
        bookToUpdate.setAuthorId(2L);

        bookDao.update(bookToUpdate);

        assertEquals(bookToUpdate, bookDao.getById(1L));
    }

    @DisplayName("Should update book name.")
    @Test
    void shouldUpdateBookName() {
        Book expectedBook = getPulpFictionBook();

        expectedBook.setName("Pulp fiction v2");

        bookDao.updateName(expectedBook.getId(), expectedBook.getName());
        assertEquals(expectedBook, bookDao.getById(expectedBook.getId()));
    }

    @DisplayName("Should delete book by id.")
    @Test
    void shouldDeleteBookById() {
        Long id = 1L;
        bookDao.deleteById(id);

        assertThatThrownBy(() -> bookDao.getById(id)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("Should throw an exception if update non-existent book.")
    @Test
    void shouldThrowExceptionIfUpdateNonExistentBook() {
        assertThatThrownBy(() -> bookDao.update(new Book(Long.MAX_VALUE, "name", 1L)))
                .isInstanceOf(JdbcUpdateAffectedIncorrectNumberOfRowsException.class);
    }

    private List<Book> getExpectedBooksList() {
        return of(
                getPulpFictionBook(),
                new Book(2L, "How to learn C++ in 24 hours", 1L, emptyList()),
                new Book(3L, "Tips for surviving on mars", 2L, emptyList())
        );
    }

    private Book getPulpFictionBook() {
        return new Book(1L, "Pulp fiction", 1L, emptyList());
    }
}