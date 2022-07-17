package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Genre;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Stream;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(BooksServiceJpa.class)
class BooksServiceJpaTest {

    @Autowired
    private BooksServiceJpa booksService;

    @Autowired
    private TestEntityManager testEntityManager;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("Should rollback transaction when inserting book with incorrect relations.")
    @ParameterizedTest
    @MethodSource("provideArgumentsForShouldRollbackTransactionWhenInsertingBookWithIncorrectRelations")
    void shouldRollbackTransactionWhenInsertingBookWithIncorrectRelations(Book book) {
        Book expectedBook = booksService.getById(book.getId()).orElseThrow();
        assertThatThrownBy(() -> booksService.save(book)).getRootCause().isInstanceOf(EntityNotFoundException.class);

        assertThat(booksService.getById(1L))
                .get()
                .usingRecursiveComparison()
                .ignoringFields("comments")
                .isEqualTo(expectedBook);
    }

    @DisplayName("Should update book name.")
    @Test
    void shouldUpdateBookName() {
        Long id = 1L;
        Book expectedBook = booksService.getById(id).orElseThrow();
        testEntityManager.clear();
        expectedBook.setName("Pulp fiction v2");

        booksService.updateNameById(id, expectedBook.getName());
        testEntityManager.flush();
        testEntityManager.clear();

        assertThat(booksService.getById(id)).get().isEqualTo(expectedBook);
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