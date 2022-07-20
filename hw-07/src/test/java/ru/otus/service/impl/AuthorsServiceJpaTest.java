package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AuthorsServiceJpa.class)
class AuthorsServiceJpaTest {

    @Autowired
    private AuthorsServiceJpa authorsService;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("Should update author full name.")
    @Test
    void shouldUpdateAuthorFullName() {
        var id = 1L;
        var expectedAuthor = authorsService.getById(id).orElseThrow();
        var originalName = expectedAuthor.getFullName();
        expectedAuthor.setFullName("Ivan Ivanovich Alexandrov");

        authorsService.updateFullNameById(id, expectedAuthor.getFullName());

        assertThat(authorsService.getById(id))
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedAuthor);

        authorsService.updateFullNameById(id, originalName);
    }
}