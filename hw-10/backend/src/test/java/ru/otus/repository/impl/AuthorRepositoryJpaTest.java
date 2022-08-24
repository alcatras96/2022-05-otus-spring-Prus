package ru.otus.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @DisplayName("Should insert author.")
    @Test
    void shouldInsertAuthor() {
        var author = authorRepository.save(new Author("Maxim Maximovich Maximov"));

        assertThat(authorRepository.findById(author.getId()))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(author);

        authorRepository.deleteById(author.getId());
    }

    @DisplayName("Should return expected authors list.")
    @Test
    void shouldReturnExpectedAuthorsList() {
        assertThat(authorRepository.findAll())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getExpectedAuthorsList());
    }

    @DisplayName("Should return expected author by full name.")
    @Test
    void shouldReturnExpectedAuthorById() {
        assertThat(authorRepository.findByFullName("author 1"))
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Author("author 1"));
    }

    @DisplayName("Should update author properly.")
    @Test
    void shouldUpdateAuthorProperly() {
        var author = authorRepository.save(new Author("test author 1"));

        author.setFullName("updated name");
        authorRepository.save(author);

        assertThat(authorRepository.findByFullName("updated name"))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(author);

        authorRepository.deleteById(author.getId());
    }

    @DisplayName("Should delete author by id with all relations.")
    @Test
    void shouldDeleteAuthorById() {
        var author = authorRepository.save(new Author("author 123"));
        var book = bookRepository.save(Book.builder().name("book 123").author(author).build());

        var authorFromDB = authorRepository.findById(author.getId());
        assertThat(authorFromDB).isPresent();
        authorRepository.deleteByIdWithRelations(authorFromDB.get().getId());
        assertThat(authorRepository.findById(author.getId())).isNotPresent();
        assertThat(bookRepository.findById(book.getId())).isNotPresent();
    }

    private List<Author> getExpectedAuthorsList() {
        return of(new Author("author 1"), new Author("author 2"));
    }
}