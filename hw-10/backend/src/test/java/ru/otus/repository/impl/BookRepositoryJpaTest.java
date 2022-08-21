package ru.otus.repository.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Comment;
import ru.otus.model.Genre;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.repository.api.BookRepository;
import ru.otus.repository.api.GenreRepository;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class BookRepositoryJpaTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("Should correctly insert a book with relations.")
    @Test
    void shouldInsertBook() {
        var genres = of(
                genreRepository.findByName("drama").orElseThrow(),
                genreRepository.findByName("comedy").orElseThrow()
        );

        var book = Book.builder()
                .name("book")
                .genres(genres)
                .author(authorRepository.findByFullName("author 1").orElseThrow())
                .build();

        bookRepository.save(book);

        assertThat(bookRepository.findById(book.getId()))
                .get()
                .usingRecursiveComparison()
                .ignoringFields("comments")
                .isEqualTo(book);

        bookRepository.delete(book);
    }

    @DisplayName("Should return expected books list.")
    @Test
    void shouldReturnExpectedBooksList() {
        assertThat(bookRepository.findAll())
                .usingRecursiveComparison()
                .ignoringFields("comments")
                .ignoringExpectedNullFields()
                .isEqualTo(getExpectedBooksList());
    }


    @DisplayName("Should return expected book by name.")
    @Test
    void shouldReturnExpectedBookById() {
        assertThat(bookRepository.findByName("book 1"))
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getFirstBook());
    }

    @DisplayName("Should update book properly.")
    @Test
    void shouldUpdateBookProperly() {
        var book = createBook();

        book.setName("Pulp fiction v2");
        book.setAuthor(authorRepository.findByFullName("author 2").orElseThrow());

        bookRepository.save(book);

        assertThat(bookRepository.findById(book.getId()))
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(book);

        bookRepository.delete(book);
    }

    @DisplayName("Should delete book by id.")
    @Test
    void shouldDeleteBookById() {
        var book = createBook();

        var bookFromDB = bookRepository.findById(book.getId());
        Assertions.assertThat(bookFromDB).isPresent();
        bookRepository.deleteById(bookFromDB.get().getId());
        Assertions.assertThat(bookRepository.findById(book.getId())).isNotPresent();
    }

    @DisplayName("Should return expected comments by book name.")
    @Test
    void shouldReturnExpectedCommentsByBookId() {
        Assertions.assertThat(bookRepository.findByName("book 1").orElseThrow().getComments())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getFirstBookComments());
    }

    private List<Comment> getFirstBookComments() {
        return of(
                new Comment("comment 1"),
                new Comment("comment 2"),
                new Comment("comment 3")
        );
    }

    private List<Book> getExpectedBooksList() {
        return of(
                getFirstBook(),
                Book.builder()
                        .name("book 2")
                        .author(new Author("author 1"))
                        .genres(of(new Genre("drama")))
                        .build(),
                Book.builder()
                        .name("book 3")
                        .author(new Author("author 2"))
                        .genres(of(new Genre("action")))
                        .build()
        );
    }

    private Book createBook() {
        var book = Book.builder()
                .name("book 1")
                .author(authorRepository.findByFullName("author 1").orElseThrow())
                .genres(of(getGenreByName("comedy"), getGenreByName("drama")))
                .build();

        return bookRepository.save(book);
    }

    private Book getFirstBook() {
        return Book.builder()
                .name("book 1")
                .author(new Author("author 1"))
                .genres(of(new Genre("comedy"), new Genre("drama")))
                .build();
    }

    private Genre getGenreByName(String name) {
        return genreRepository.findByName(name).orElseThrow();
    }
}