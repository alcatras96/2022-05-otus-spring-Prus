package ru.otus.repository.impl;

import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;
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

    @Autowired
    private MongockInitializingBeanRunner mongockInitializingBeanRunner;

    @DisplayName("Should insert genre.")
    @Test
    void shouldInsertGenre() {
        var genreToInsert = new Genre("test genre");

        genreRepository.save(genreToInsert).block();

        StepVerifier
                .create(genreRepository.findById(genreToInsert.getId()))
                .assertNext(genre ->
                        assertThat(genre)
                                .usingRecursiveComparison()
                                .isEqualTo(genreToInsert)
                )
                .expectComplete()
                .verify();

        genreRepository.deleteById(genreToInsert.getId()).block();
    }

    @DisplayName("Should return expected genres list.")
    @Test
    void shouldReturnExpectedGenresList() {
        StepVerifier
                .create(genreRepository.findAll().collectList())
                .assertNext(genres ->
                        assertThat(genres)
                                .usingRecursiveComparison()
                                .ignoringExpectedNullFields()
                                .isEqualTo(getExpectedGenresList()))
                .expectComplete()
                .verify();
    }

    @DisplayName("Should return expected genre by name.")
    @Test
    void shouldReturnExpectedGenreById() {
        StepVerifier
                .create(genreRepository.findByName("comedy"))
                .assertNext(genre ->
                        assertThat(genre)
                                .usingRecursiveComparison()
                                .ignoringExpectedNullFields()
                                .isEqualTo(new Genre("comedy")))
                .expectComplete()
                .verify();
    }

    @DisplayName("Should update genre properly.")
    @Test
    void shouldUpdateGenreProperly() {
        var newGenre = genreRepository.save(new Genre("genre 123")).block();

        newGenre.setName("updated genre");
        genreRepository.save(newGenre).block();

        StepVerifier
                .create(genreRepository.findById(newGenre.getId()))
                .assertNext(genre ->
                        Assertions.assertThat(genre)
                                .usingRecursiveComparison()
                                .isEqualTo(newGenre))
                .expectComplete()
                .verify();


        genreRepository.deleteById(newGenre.getId()).block();
    }

    @DisplayName("Should delete genre by id with all relations.")
    @Test
    void shouldDeleteGenreById() {
        var newGenre = genreRepository.save(new Genre("genre 123")).block();
        var newBook = bookRepository.save(Book.builder().name("book 123").genreIds(of(newGenre.getId())).build()).block();

        genreRepository.deleteByIdWithRelations(newGenre.getId());

        StepVerifier
                .create(genreRepository.findById(newGenre.getId()))
                .assertNext(genre -> Assertions.assertThat(newBook).isNotNull())
                .expectComplete()
                .verify();

        genreRepository.deleteByIdWithRelations(newGenre.getId()).block();

        StepVerifier
                .create(genreRepository.findById(newGenre.getId()))
                .expectNextCount(0)
                .expectComplete()
                .verify();

        StepVerifier
                .create(bookRepository.findById(newBook.getId()))
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

    private List<Genre> getExpectedGenresList() {
        return of(
                new Genre("comedy"),
                new Genre("drama"),
                new Genre("action")
        );
    }
}