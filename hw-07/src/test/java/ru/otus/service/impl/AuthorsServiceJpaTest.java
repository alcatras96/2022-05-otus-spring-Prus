package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AuthorsServiceJpa.class)
class AuthorsServiceJpaTest {

    @Autowired
    private AuthorsServiceJpa authorsService;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Should update author full name.")
    @Test
    void shouldUpdateAuthorFullName() {
        var id = 1L;
        var expectedAuthor = authorsService.getById(id).orElseThrow();
        testEntityManager.clear();
        expectedAuthor.setFullName("Ivan Ivanovich Alexandrov");

        authorsService.updateFullNameById(id, expectedAuthor.getFullName());
        testEntityManager.flush();
        testEntityManager.clear();

        assertThat(authorsService.getById(id))
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedAuthor);
    }
}