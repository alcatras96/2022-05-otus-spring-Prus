package ru.otus.repository.impl;

import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.model.Book;
import ru.otus.model.Comment;
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

    @Autowired
    private MongockInitializingBeanRunner mongockInitializingBeanRunner;


    @DisplayName("Should correctly insert a book with relations.")
    @Test
    void shouldInsertBook() {
        var genres = Mono.zip(
                genreRepository.findByName("drama"),
                genreRepository.findByName("comedy")
        ).map(genresTuple -> List.of(genresTuple.getT1(), genresTuple.getT2())).block();

        var newBook = Book.builder()
                .name("book")
                .genres(genres)
                .author(authorRepository.findByFullName("author 1").block())
                .build();

        bookRepository.save(newBook).block();

        StepVerifier
                .create(bookRepository.findById(newBook.getId()))
                .assertNext(book ->
                        assertThat(book)
                                .usingRecursiveComparison()
                                .ignoringFields("comments")
                                .isEqualTo(book)
                )
                .expectComplete()
                .verify();

        bookRepository.delete(newBook).block();
    }

    @DisplayName("Should return expected books list.")
    @Test
    void shouldReturnExpectedBooksList() {
        StepVerifier
                .create(bookRepository.findAll().collectList())
                .assertNext(books ->
                        assertThat(books)
                                .usingRecursiveComparison()
                                .ignoringFields("comments")
                                .ignoringExpectedNullFields()
                                .isEqualTo(getExpectedBooksList()))
                .expectComplete()
                .verify();
    }


    @DisplayName("Should return expected book by name.")
    @Test
    void shouldReturnExpectedBookById() {
        StepVerifier
                .create(bookRepository.findByName("book 1"))
                .assertNext(book -> {
                    assertThat(book)
                            .usingRecursiveComparison()
                            .ignoringExpectedNullFields()
                            .isEqualTo(getFirstBook());
                })

                .expectComplete()
                .verify();
    }

    @DisplayName("Should update book properly.")
    @Test
    void shouldUpdateBookProperly() {
        var newBook = createBook();

        newBook.setName("Pulp fiction v2");
        newBook.setAuthor(authorRepository.findByFullName("author 2").block());

        bookRepository.save(newBook).block();

        StepVerifier
                .create(bookRepository.findById(newBook.getId()))
                .assertNext(book ->
                        assertThat(bookRepository.findById(newBook.getId()))
                                .usingRecursiveComparison()
                                .ignoringExpectedNullFields()
                                .isEqualTo(newBook))
                .expectComplete()
                .verify();


        bookRepository.delete(newBook).block();
    }

    @DisplayName("Should delete book by id.")
    @Test
    void shouldDeleteBookById() {
        var newBook = createBook();
        var bookFromDB = bookRepository.findById(newBook.getId()).block();

        Assertions.assertThat(bookFromDB).isNotNull();
        bookRepository.deleteById(bookFromDB.getId()).block();

        StepVerifier
                .create(bookRepository.findById(newBook.getId()))
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

    @DisplayName("Should return expected comments by book name.")
    @Test
    void shouldReturnExpectedCommentsByBookId() {
        StepVerifier
                .create(bookRepository.findByName("book 1"))
                .assertNext(book ->
                        Assertions.assertThat(book.getComments())
                                .usingRecursiveComparison()
                                .ignoringExpectedNullFields()
                                .isEqualTo(getFirstBookComments())
                )
                .expectComplete()
                .verify();
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
                        .authorId(authorRepository.findByFullName("author 1").block().getId())
                        .genreIds(of(getGenreIdByName("drama")))
                        .build(),
                Book.builder()
                        .name("book 3")
                        .authorId(authorRepository.findByFullName("author 2").block().getId())
                        .genreIds(of(getGenreIdByName("action")))
                        .build()
        );
    }

    private Book createBook() {
        var book = Book.builder()
                .name("book 10")
                .authorId(authorRepository.findByFullName("author 1").block().getId())
                .genreIds(of(getGenreIdByName("comedy"), getGenreIdByName("drama")))
                .build();

        return bookRepository.save(book).block();
    }

    private Book getFirstBook() {
        return Book.builder()
                .name("book 1")
                .authorId(authorRepository.findByFullName("author 1").block().getId())
                .genreIds(of(getGenreIdByName("comedy"), getGenreIdByName("drama")))
                .build();
    }

    private String getGenreIdByName(String name) {
        return genreRepository.findByName(name).block().getId();
    }
}