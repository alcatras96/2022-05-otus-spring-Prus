package ru.otus.repository.impl;

import org.hibernate.SessionFactory;
import org.hibernate.TransientObjectException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Genre;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(BookRepositoryJpa.class)
class BookRepositoryJpaTest {

    @Autowired
    private BookRepositoryJpa bookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Should correctly insert a book with relations.")
    @Test
    void shouldInsertBook() {
        EntityManager entityManager = testEntityManager.getEntityManager();
        List<Genre> genres = of(
                entityManager.find(Genre.class, 1L),
                entityManager.find(Genre.class, 2L)
        );
        Book book = Book.builder()
                .name("book")
                .genres(genres)
                .author(entityManager.find(Author.class, 1L))
                .build();

        bookRepository.insert(book);
        testEntityManager.flush();
        testEntityManager.clear();

        assertThat(bookRepository.getById(book.getId()))
                .usingRecursiveComparison()
                .isEqualTo(book);
    }

    @DisplayName("Should throw an exception for incorrect relations when inserting a book.")
    @ParameterizedTest
    @MethodSource("provideArgumentsForShouldThrowExceptionForIncorrectRelationsWhenInsertingBook")
    void shouldThrowExceptionForIncorrectRelationsWhenInsertingBook(Book book) {
        EntityManager entityManager = testEntityManager.getEntityManager();

        book.setAuthor(entityManager.getReference(Author.class, book.getAuthor().getId()));
        book.setGenres(
                book.getGenres()
                        .stream()
                        .map(Genre::getId)
                        .map(id -> entityManager.getReference(Genre.class, id))
                        .collect(Collectors.toList())
        );

        assertThatThrownBy(() -> {
            bookRepository.insert(book);
            testEntityManager.flush();
        }).hasCauseInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("Should insert book only.")
    @Test
    void shouldInsertBookOnly() {
        EntityManager entityManager = testEntityManager.getEntityManager();

        List<Book> books = of(
                new Book(
                        "new book with new author",
                        new Author("New Author"),
                        of(entityManager.getReference(Genre.class, 1L))
                ),
                new Book(
                        "new book with new genre",
                        entityManager.getReference(Author.class, 1L),
                        of(new Genre("new genre"))
                )
        );

        books.forEach(book ->
                assertThatThrownBy(() -> {
                    bookRepository.insert(book);
                    testEntityManager.flush();
                }).hasCauseInstanceOf(TransientObjectException.class)
        );
    }

    @DisplayName("Should return expected books list.")
    @Test
    void shouldReturnExpectedBooksList() {
        assertThat(bookRepository.getAll())
                .usingRecursiveComparison()
                .isEqualTo(getExpectedBooksList());
    }

    @DisplayName("Should return expected book list without N+1 problem")
    @Test
    void shouldReturnExpectedBooksListWithoutManyRequests() {
        SessionFactory sessionFactory = testEntityManager
                .getEntityManager()
                .getEntityManagerFactory()
                .unwrap(SessionFactory.class);

        Statistics statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);

        assertThat(bookRepository.getAll()).usingRecursiveComparison().isEqualTo(getExpectedBooksList());

        assertThat(statistics.getPrepareStatementCount()).isEqualTo(2);
    }

    @DisplayName("Should return expected book by id.")
    @Test
    void shouldReturnExpectedBookById() {
        assertEquals(getPulpFictionBook(), bookRepository.getById(1L));
    }

    @DisplayName("Should update book properly.")
    @Test
    void shouldUpdateBookProperly() {
        Book bookToUpdate = testEntityManager.find(Book.class, getPulpFictionBook().getId());

        bookToUpdate.setName("Pulp fiction v2");
        bookToUpdate.setAuthor(new Author(2L));

        bookRepository.update(bookToUpdate);
        testEntityManager.flush();
        testEntityManager.clear();

        assertEquals(bookToUpdate, bookRepository.getById(1L));
    }

    @DisplayName("Should update book name.")
    @Test
    void shouldUpdateBookName() {
        Book expectedBook = getPulpFictionBook();

        expectedBook.setName("Pulp fiction v2");

        bookRepository.updateName(expectedBook.getId(), expectedBook.getName());
        testEntityManager.flush();
        testEntityManager.clear();

        assertEquals(expectedBook, bookRepository.getById(expectedBook.getId()));
    }

    @DisplayName("Should delete book by id.")
    @Test
    void shouldDeleteBookById() {
        Long id = 1L;
        assertThat(bookRepository.getById(id)).isNotNull();
        bookRepository.deleteById(id);
        testEntityManager.flush();
        assertThat(bookRepository.getById(id)).isNull();
    }

    private List<Book> getExpectedBooksList() {
        return of(
                getPulpFictionBook(),
                new Book(2L, "How to learn C++ in 24 hours", new Author(1L, "Ivan Ivanovich Ivanov"),
                        of(new Genre(1L, "comedy"), new Genre(2L, "drama"))),
                new Book(3L, "History of Ancient Rome", new Author(1L, "Ivan Ivanovich Ivanov"),
                        of(new Genre(3L, "action"))),
                new Book(4L, "Tips for surviving on mars", new Author(2L, "Alexander Alexandrovich Alexandrov"),
                        of(new Genre(4L, "science")))
        );
    }

    private Book getPulpFictionBook() {
        return new Book(1L, "Pulp fiction", new Author(1L, "Ivan Ivanovich Ivanov"),
                of(new Genre(1L, "comedy"), new Genre(2L, "drama"), new Genre(3L, "action")));
    }

    public static Stream<Arguments> provideArgumentsForShouldThrowExceptionForIncorrectRelationsWhenInsertingBook() {
        return Stream.of(
                Arguments.of(new Book("new book", new Author(Long.MAX_VALUE), of(new Genre(1L)))),
                Arguments.of(new Book("new book", new Author(1L), of(new Genre(Long.MAX_VALUE))))
        );
    }
}