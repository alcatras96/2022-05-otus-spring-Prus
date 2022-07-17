package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(GenresServiceJpa.class)
class GenresServiceJpaTest {

    @Autowired
    private GenresServiceJpa genresService;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Should update genre name.")
    @Test
    void shouldUpdateGenreName() {
        var id = 1L;
        var expectedGenre = testEntityManager.find(Genre.class, 1L);
        expectedGenre.setName("tragedy");
        testEntityManager.clear();

        genresService.updateNameById(id, expectedGenre.getName());
        testEntityManager.flush();
        testEntityManager.clear();

        assertThat(genresService.getById(id))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }
}