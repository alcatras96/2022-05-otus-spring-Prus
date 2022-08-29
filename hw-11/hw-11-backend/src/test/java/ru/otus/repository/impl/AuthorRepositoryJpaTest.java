package ru.otus.repository.impl;

import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.repository.api.BookRepository;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongockInitializingBeanRunner mongockInitializingBeanRunner;

    @DisplayName("Should insert author.")
    @Test
    void shouldInsertAuthor() {
        var authorName = "Maxim Maximovich Maximov";
        var createdAuthor = authorRepository.save(new Author(authorName)).block();

        StepVerifier.create(authorRepository.findById(createdAuthor.getId()))
                .assertNext(author ->
                        assertThat(author)
                                .usingRecursiveComparison()
                                .isEqualTo(author))
                .expectComplete()
                .verify();

        authorRepository.deleteById(createdAuthor.getId()).block();
    }

    @DisplayName("Should return expected authors list.")
    @Test
    void shouldReturnExpectedAuthorsList() {
        StepVerifier.create(authorRepository.findAll().collectList())
                .assertNext(authors -> assertThat(authors)
                        .usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(getExpectedAuthorsList())
                )
                .expectComplete()
                .verify();
    }

    @DisplayName("Should return expected author by full name.")
    @Test
    void shouldReturnExpectedAuthorById() {
        var authorName = "author 1";
        StepVerifier
                .create(authorRepository.findByFullName(authorName))
                .assertNext(author ->
                        assertThat(author)
                                .usingRecursiveComparison()
                                .ignoringExpectedNullFields()
                                .isEqualTo(new Author("author 1")))
                .expectComplete()
                .verify();
    }

    @DisplayName("Should update author properly.")
    @Test
    void shouldUpdateAuthorProperly() {
        var newAuthor = authorRepository.save(new Author("test author 1")).block();

        var newAuthorName = "updated name";
        newAuthor.setFullName(newAuthorName);
        authorRepository.save(newAuthor).block();

        StepVerifier
                .create(authorRepository.findByFullName(newAuthorName))
                .assertNext(author ->
                        assertThat(author)
                                .usingRecursiveComparison()
                                .isEqualTo(author))
                .expectComplete()
                .verify();

        authorRepository.deleteById(newAuthor.getId()).block();
    }

    @DisplayName("Should delete author by id with all relations.")
    @Test
    void shouldDeleteAuthorById() {
        var newAuthor = authorRepository.save(new Author("author 123")).block();
        var newBook = bookRepository.save(Book.builder().name("book 123").authorId(newAuthor.getId()).build()).block();

        StepVerifier
                .create(authorRepository.findById(newAuthor.getId()))
                .assertNext(author -> assertThat(author).isNotNull())
                .expectComplete()
                .verify();

        authorRepository.deleteByIdWithRelations(newAuthor.getId()).block();

        StepVerifier
                .create(bookRepository.findById(newBook.getId()))
                .expectNextCount(0)
                .expectComplete()
                .verify();

        StepVerifier
                .create(authorRepository.findById(newAuthor.getId()))
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

    private List<Author> getExpectedAuthorsList() {
        return of(new Author("author 1"), new Author("author 2"));
    }
}