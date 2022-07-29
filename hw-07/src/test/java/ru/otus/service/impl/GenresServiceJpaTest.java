package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(GenresServiceJpa.class)
class GenresServiceJpaTest {

    @Autowired
    private GenresServiceJpa genresService;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("Should update genre name.")
    @Test
    void shouldUpdateGenreName() {
        var id = 1L;
        var updatedName = "tragedy";
        var originalName = genresService.getById(id).orElseThrow().getName();

        genresService.updateNameById(id, updatedName);

        assertThat(genresService.getById(id))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(new Genre(1L, updatedName));

        genresService.updateNameById(id, originalName);
    }
}