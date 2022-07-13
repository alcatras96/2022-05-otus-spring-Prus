package ru.otus.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.model.Genre;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(GenreRepositoryJpa.class)
class GenreRepositoryJpaTest {

    @Autowired
    private GenreRepositoryJpa genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("Should insert genre.")
    @Test
    void shouldInsertGenre() {
        Genre genreToInsert = new Genre("test genre");

        genreRepository.insert(genreToInsert);
        entityManager.clear();

        assertThat(genreRepository.getById(genreToInsert.getId()))
                .usingRecursiveComparison()
                .isEqualTo(genreToInsert);
    }

    @DisplayName("Should return expected genres list.")
    @Test
    void shouldReturnExpectedGenresList() {
        assertThat(genreRepository.getAll())
                .usingRecursiveComparison()
                .isEqualTo(getExpectedGenresList());
    }

    @DisplayName("Should return expected genre by id.")
    @Test
    void shouldReturnExpectedGenreById() {
        assertThat(genreRepository.getById(1L))
                .usingRecursiveComparison()
                .isEqualTo(new Genre(1L, "comedy"));
    }

    @DisplayName("Should update genre properly.")
    @Test
    void shouldUpdateGenreProperly() {
        Genre updatedGenre = entityManager.find(Genre.class, 1L);
        updatedGenre.setName("tragedy");

        genreRepository.update(updatedGenre);
        entityManager.flush();
        entityManager.clear();

        assertThat(genreRepository.getById(1L))
                .usingRecursiveComparison()
                .isEqualTo(updatedGenre);
    }

    @DisplayName("Should update genre name.")
    @Test
    void shouldUpdateGenreName() {
        String updatedName = "tragedy";
        Long id = 1L;

        genreRepository.updateNameById(id, updatedName);

        assertThat(genreRepository.getById(id))
                .usingRecursiveComparison()
                .isEqualTo(new Genre(1L, "tragedy"));
    }

    @DisplayName("Should delete genre by id.")
    @Test
    void shouldDeleteGenreById() {
        Long id = 1L;
        assertThat(genreRepository.getById(id)).isNotNull();
        genreRepository.deleteById(id);
        entityManager.clear();
        assertThat(genreRepository.getById(id)).isNull();
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