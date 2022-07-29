package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.model.Comment;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(BooksServiceMongo.class)
class BooksServiceTest {

    @Autowired
    private BooksServiceMongo booksServiceMongo;

    @DisplayName("Should return expected comments by book name.")
    @Test
    void shouldReturnExpectedCommentsByBookId() {
        assertThat(booksServiceMongo.getByName("book 1").orElseThrow().getComments())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getFirstBookComments());
    }

    @DisplayName("Should delete comments by book id.")
    @Test
    void shouldDeleteCommentsByBookId() {
        var book = booksServiceMongo.getByName("book 1").orElseThrow();
        booksServiceMongo.deleteCommentsById(book.getId());

        assertThat(booksServiceMongo.getByName("book 1").orElseThrow().getComments()).isEmpty();

        getFirstBookComments().forEach(comment -> booksServiceMongo.addComment(book.getId(), comment));
    }

    @DisplayName("Should create comment successfully.")
    @Test
    void shouldCreateCommentSuccessfully() {
        var bookWithOldComments = booksServiceMongo.getByName("book 1").orElseThrow();
        var newComment = new Comment("comment new");
        booksServiceMongo.addComment(bookWithOldComments.getId(), newComment);

        var expectedComments = new java.util.ArrayList<>(getFirstBookComments());
        expectedComments.add(newComment);
        assertThat(booksServiceMongo.getByName("book 1").orElseThrow().getComments())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedComments);

        booksServiceMongo.save(bookWithOldComments);
    }


    private List<Comment> getFirstBookComments() {
        return of(
                new Comment("comment 1"),
                new Comment("comment 2"),
                new Comment("comment 3")
        );
    }
}