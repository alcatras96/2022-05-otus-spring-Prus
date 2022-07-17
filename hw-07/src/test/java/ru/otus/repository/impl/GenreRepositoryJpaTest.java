package ru.otus.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.model.Genre;
import ru.otus.repository.api.GenreRepository;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GenreRepositoryJpaTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("Should insert genre.")
    @Test
    void shouldInsertGenre() {
        Genre genreToInsert = new Genre("test genre");

        genreRepository.save(genreToInsert);
        entityManager.clear();

        assertThat(genreRepository.findById(genreToInsert.getId()))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(genreToInsert);
    }

    @DisplayName("Should return expected genres list.")
    @Test
    void shouldReturnExpectedGenresList() {
        assertThat(genreRepository.findAll())
                .usingRecursiveComparison()
                .isEqualTo(getExpectedGenresList());
    }

    @DisplayName("Should return expected genre by id.")
    @Test
    void shouldReturnExpectedGenreById() {
        assertThat(genreRepository.findById(1L))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(new Genre(1L, "comedy"));
    }

    @DisplayName("Should update genre properly.")
    @Test
    void shouldUpdateGenreProperly() {
        Genre updatedGenre = entityManager.find(Genre.class, 1L);
        updatedGenre.setName("tragedy");

        genreRepository.save(updatedGenre);
        entityManager.flush();
        entityManager.clear();

        assertThat(genreRepository.findById(1L))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(updatedGenre);
    }

    @DisplayName("Should delete genre by id.")
    @Test
    void shouldDeleteGenreById() {
        Long id = 1L;
        assertThat(genreRepository.findById(id)).isPresent();
        genreRepository.deleteById(id);
        entityManager.flush();
        assertThat(genreRepository.findById(id)).isNotPresent();
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
}