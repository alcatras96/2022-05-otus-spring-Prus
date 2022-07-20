package ru.otus.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.model.Book;
import ru.otus.model.Comment;
import ru.otus.repository.api.CommentRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryJpaTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Should insert comment.")
    @Test
    void shouldInsertComment() {
        EntityManager entityManager = this.testEntityManager.getEntityManager();
        Comment commentForInsert = new Comment(entityManager.getReference(Book.class, 1L), "comment text");

        commentRepository.save(commentForInsert);
        testEntityManager.clear();

        assertThat(commentRepository.findById(commentForInsert.getId()))
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Comment(commentForInsert.getId(), 1L, "Pulp fiction", "comment text"));
    }

    @DisplayName("Should return expected comments list.")
    @Test
    void shouldReturnExpectedCommentsList() {
        assertThat(commentRepository.findAll())
                .usingRecursiveComparison()
                .isEqualTo(getExpectedCommentsList());
    }

    @DisplayName("Should return expected comment by id.")
    @Test
    void shouldReturnExpectedCommentById() {
        assertThat(commentRepository.findById(1L))
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Comment(1L, 1L, "Pulp fiction", "comment 1"));
    }

    @DisplayName("Should update comment properly.")
    @Test
    void shouldUpdateCommentProperly() {
        Comment comment = testEntityManager.find(Comment.class, 1L);
        comment.setText("comment 1 updated");

        commentRepository.save(comment);
        testEntityManager.flush();
        testEntityManager.clear();

        assertThat(commentRepository.findById(comment.getId()))
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Comment(comment.getId(), 1L, "Pulp fiction", "comment 1 updated"));
    }

    @DisplayName("Should delete comment by id.")
    @Test
    void shouldDeleteCommentById() {
        Long id = 1L;
        assertThat(commentRepository.findById(id)).isPresent();
        commentRepository.deleteById(id);
        testEntityManager.flush();
        assertThat(commentRepository.findById(id)).isNotPresent();
    }

    private List<Comment> getExpectedCommentsList() {
        return of(
                new Comment(1L, 1L, "Pulp fiction", "comment 1"),
                new Comment(2L, 1L, "Pulp fiction", "comment 2"),
                new Comment(3L, 1L, "Pulp fiction", "comment 3"),
                new Comment(4L, 2L, "How to learn C++ in 24 hours", "comment 4")
        );
    }
}