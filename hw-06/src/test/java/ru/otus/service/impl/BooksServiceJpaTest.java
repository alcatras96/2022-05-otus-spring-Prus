package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Genre;
import ru.otus.repository.api.BookRepository;
import ru.otus.repository.impl.AuthorRepositoryJpa;
import ru.otus.repository.impl.BookRepositoryJpa;
import ru.otus.repository.impl.GenreRepositoryJpa;

import javax.persistence.PersistenceException;
import java.util.stream.Stream;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@DataJpaTest
@Import({BooksServiceJpa.class, BookRepositoryJpa.class, GenreRepositoryJpa.class, AuthorRepositoryJpa.class})
class BooksServiceJpaTest {

    @Autowired
    private BooksServiceJpa booksService;

    @SpyBean
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("Should save book correctly.")
    @Test
    void shouldSaveBookCorrectly() {
        Book book = Book.builder()
                .name("new book")
                .author(new Author(1L))
                .genres(of(new Genre(1L)))
                .build();

        booksService.save(book);
        verify(bookRepository).insert(book);
        reset(bookRepository);
        entityManager.clear();

        booksService.save(book);
        verify(bookRepository).update(book);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("Should rollback transaction when inserting book with incorrect relations.")
    @ParameterizedTest
    @MethodSource("provideArgumentsForShouldRollbackTransactionWhenInsertingBookWithIncorrectRelations")
    void shouldRollbackTransactionWhenInsertingBookWithIncorrectRelations(Book book) {
        Book expectedBook = booksService.getById(book.getId());
        assertThatThrownBy(() -> booksService.save(book)).isInstanceOf(PersistenceException.class);

        assertThat(booksService.getById(1L))
                .usingRecursiveComparison()
                .ignoringFields("comments")
                .isEqualTo(expectedBook);
    }

    public static Stream<Arguments> provideArgumentsForShouldRollbackTransactionWhenInsertingBookWithIncorrectRelations() {
        return Stream.of(
                Arguments.of(Book.builder()
                        .id(1L)
                        .name("book")
                        .author(new Author(Long.MAX_VALUE))
                        .build()
                ),
                Arguments.of(Book.builder()
                        .id(1L)
                        .name("book")
                        .author(new Author(1L))
                        .genres(of(new Genre(Long.MAX_VALUE)))
                        .build()
                )
        );
    }
}