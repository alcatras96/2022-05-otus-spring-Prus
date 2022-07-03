package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dao.api.AuthorDao;
import ru.otus.domain.Author;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = AuthorsServiceImpl.class)
class AuthorsServiceImplTest {

    @Autowired
    private AuthorsServiceImpl authorsService;

    @MockBean
    private AuthorDao authorDao;

    @DisplayName("Should save author correctly.")
    @Test
    void shouldSaveAuthorCorrectly() {
        Author author = new Author("Ivan Ivanovich Iv");

        authorsService.save(author);
        verify(authorDao, times(1)).insert(author);
        reset(authorDao);

        author.setId(5L);
        authorsService.save(author);
        verify(authorDao, times(1)).update(author);
    }
}