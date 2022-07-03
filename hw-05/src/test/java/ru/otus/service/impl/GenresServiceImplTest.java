package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dao.api.GenreDao;
import ru.otus.domain.Genre;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = GenresServiceImpl.class)
class GenresServiceImplTest {

    @Autowired
    private GenresServiceImpl genresService;

    @MockBean
    private GenreDao genreDao;

    @DisplayName("Should save genre correctly.")
    @Test
    void shouldSaveGenreCorrectly() {
        Genre genre = new Genre("new genre");

        genresService.save(genre);
        verify(genreDao, times(1)).insert(genre);
        reset(genreDao);

        genre.setId(5L);
        genresService.save(genre);
        verify(genreDao, times(1)).update(genre);
    }
}