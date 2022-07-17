package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.model.Author;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = AuthorsServiceJpa.class)
class AuthorsServiceJpaTest {

    @Autowired
    private AuthorsServiceJpa authorsService;

    @MockBean
    private AuthorRepository authorRepository;

    @DisplayName("Should save author correctly.")
    @Test
    void shouldSaveAuthorCorrectly() {
        Author author = new Author("Ivan Ivanovich Iv");

        authorsService.save(author);
        verify(authorRepository).insert(author);
        reset(authorRepository);

        author.setId(5L);
        authorsService.save(author);
        verify(authorRepository).update(author);
    }
}