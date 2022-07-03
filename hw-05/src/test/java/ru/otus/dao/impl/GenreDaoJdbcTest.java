package ru.otus.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import ru.otus.domain.Genre;

import java.util.List;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {

    @Autowired
    private GenreDaoJdbc genreDao;

    @DisplayName("Should insert genre.")
    @Test
    void shouldInsertGenre() {
        Genre genreToInsert = new Genre("test genre");

        Long genreId = genreDao.insert(genreToInsert);
        genreToInsert.setId(genreId);

        assertEquals(genreToInsert, genreDao.getById(genreId));
    }

    @DisplayName("Should return expected genres list.")
    @Test
    void shouldReturnExpectedGenresList() {
        assertEquals(getExpectedGenresList(), genreDao.getAll());
    }

    @DisplayName("Should return expected genre by id.")
    @Test
    void shouldReturnExpectedGenreById() {
        assertEquals(new Genre(1L, "comedy"), genreDao.getById(1L));
    }

    @DisplayName("Should update genre properly.")
    @Test
    void shouldUpdateGenreProperly() {
        Genre updatedGenre = new Genre(1L, "tragedy");

        genreDao.update(updatedGenre);

        assertEquals(updatedGenre, genreDao.getById(1L));
    }

    @DisplayName("Should update genre name.")
    @Test
    void shouldUpdateGenreName() {
        String updatedName = "tragedy";
        Long id = 1L;

        genreDao.updateNameById(id, updatedName);
        assertEquals(new Genre(1L, "tragedy"), genreDao.getById(id));
    }

    @DisplayName("Should delete genre by id.")
    @Test
    void shouldDeleteGenreById() {
        Long id = 1L;
        genreDao.deleteById(id);

        assertThatThrownBy(() -> genreDao.getById(id)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("Should return all used genres.")
    @Test
    void shouldReturnAllUsedGenres() {
        List<Genre> expectedGenres = getExpectedUsedGenresList();

        assertEquals(expectedGenres, genreDao.getAllUsed());
    }

    @DisplayName("Should return genres by ids.")
    @Test
    void shouldReturnGenresByIds() {
        List<Genre> expectedGenres = of(
                new Genre(1L, "comedy"),
                new Genre(2L, "drama")
        );

        List<Long> genresIds = expectedGenres
                .stream()
                .map(Genre::getId)
                .collect(toList());

        assertEquals(expectedGenres, genreDao.getByIds(genresIds));
    }

    @DisplayName("Should throw an exception if update non-existent genre.")
    @Test
    void shouldThrowExceptionIfUpdateNonExistentGenre() {
        assertThatThrownBy(() -> genreDao.update(new Genre(Long.MAX_VALUE, "name")))
                .isInstanceOf(JdbcUpdateAffectedIncorrectNumberOfRowsException.class);
    }

    private List<Genre> getExpectedGenresList() {
        return of(
                new Genre(1L, "comedy"),
                new Genre(2L, "drama"),
                new Genre(3L, "action"),
                new Genre(4L, "science"),
                new Genre(5L, "unused genre")
        );
    }

    private List<Genre> getExpectedUsedGenresList() {
        return of(
                new Genre(1L, "comedy"),
                new Genre(2L, "drama"),
                new Genre(3L, "action"),
                new Genre(4L, "science")
        );
    }
}