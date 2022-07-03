package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.api.BookDao;
import ru.otus.dao.impl.BookDaoJdbc;
import ru.otus.dao.impl.GenreDaoJdbc;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@JdbcTest
@Import({BooksServiceJdbc.class, BookDaoJdbc.class, GenreDaoJdbc.class})
class BooksServiceJdbcTest {

    @Autowired
    private BooksServiceJdbc booksService;

    @SpyBean
    private BookDao bookDao;

    @DisplayName("Should save book correctly.")
    @Test
    void shouldSaveBookCorrectly() {
        Book book = new Book("new book", 1L);
        book.setGenres(of(new Genre(1L)));

        booksService.save(book);
        verify(bookDao, times(1)).insert(book);
        reset(bookDao);

        book.setId(1L);
        booksService.save(book);
        verify(bookDao, times(1)).update(book);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("Should rollback transaction when inserting book with incorrect relations.")
    @Test
    void shouldRollbackTransactionWhenInsertingBookWithIncorrectRelations() {
        Book expectedBook = booksService.getById(1L);
        Book book = new Book(1L, "new book", Long.MAX_VALUE);

        assertThatThrownBy(() -> booksService.save(book)).isInstanceOf(DataIntegrityViolationException.class);
        assertEquals(expectedBook, booksService.getById(1L));

        book.setAuthorId(1L);
        book.setGenres(of(new Genre(Long.MAX_VALUE)));
        assertThatThrownBy(() -> booksService.save(book)).isInstanceOf(DataIntegrityViolationException.class);
        assertEquals(expectedBook, booksService.getById(1L));
    }
}