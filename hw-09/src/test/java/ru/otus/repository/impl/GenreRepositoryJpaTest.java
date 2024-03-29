package ru.otus.repository.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.model.Book;
import ru.otus.model.Genre;
import ru.otus.repository.api.BookRepository;
import ru.otus.repository.api.GenreRepository;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class GenreRepositoryJpaTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("Should insert genre.")
    @Test
    void shouldInsertGenre() {
        Genre genreToInsert = new Genre("test genre");

        genreRepository.save(genreToInsert);

        assertThat(genreRepository.findById(genreToInsert.getId()))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(genreToInsert);

        genreRepository.deleteById(genreToInsert.getId());
    }

    @DisplayName("Should return expected genres list.")
    @Test
    void shouldReturnExpectedGenresList() {
        assertThat(genreRepository.findAll())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getExpectedGenresList());
    }

    @DisplayName("Should return expected genre by name.")
    @Test
    void shouldReturnExpectedGenreById() {
        assertThat(genreRepository.findByName("comedy"))
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Genre("comedy"));
    }

    @DisplayName("Should update genre properly.")
    @Test
    void shouldUpdateGenreProperly() {
        var genre = genreRepository.save(new Genre("genre 123"));

        genre.setName("updated genre");
        genreRepository.save(genre);

        Assertions.assertThat(genreRepository.findById(genre.getId()))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(genre);

        genreRepository.deleteById(genre.getId());
    }

    @DisplayName("Should delete genre by id with all relations.")
    @Test
    void shouldDeleteGenreById() {
        var genre = genreRepository.save(new Genre("genre 123"));
        var book = bookRepository.save(Book.builder().name("book 123").genres(of(genre)).build());

        Assertions.assertThat(genreRepository.findById(genre.getId())).isPresent();
        genreRepository.deleteByIdWithRelations(genre.getId());
        Assertions.assertThat(genreRepository.findById(genre.getId())).isNotPresent();
        Assertions.assertThat(bookRepository.findById(book.getId())).isNotPresent();
    }

    private List<Genre> getExpectedGenresList() {
        return of(
                new Genre("comedy"),
                new Genre("drama"),
                new Genre("action")
        );
    }
}