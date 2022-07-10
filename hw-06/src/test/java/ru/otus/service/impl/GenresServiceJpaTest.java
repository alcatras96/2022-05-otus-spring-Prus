package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.model.Genre;
import ru.otus.repository.api.GenreRepository;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = GenresServiceJpa.class)
class GenresServiceJpaTest {

    @Autowired
    private GenresServiceJpa genresService;

    @MockBean
    private GenreRepository genreRepository;

    @DisplayName("Should save genre correctly.")
    @Test
    void shouldSaveGenreCorrectly() {
        Genre genre = new Genre("new genre");

        genresService.save(genre);
        verify(genreRepository).insert(genre);
        reset(genreRepository);

        genre.setId(5L);
        genresService.save(genre);
        verify(genreRepository).update(genre);
    }
}