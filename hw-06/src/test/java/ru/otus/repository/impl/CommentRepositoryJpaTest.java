package ru.otus.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.model.Book;
import ru.otus.model.Comment;

import javax.persistence.EntityManager;
import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CommentRepositoryJpa.class)
class CommentRepositoryJpaTest {

    @Autowired
    private CommentRepositoryJpa commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Should insert comment.")
    @Test
    void shouldInsertComment() {
        EntityManager entityManager = this.testEntityManager.getEntityManager();
        Comment commentForInsert = new Comment(entityManager.getReference(Book.class, 1L), "comment text");

        commentRepository.insert(commentForInsert);

        assertThat(commentRepository.getById(commentForInsert.getId()))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Comment(commentForInsert.getId(), "Pulp fiction", "comment text"));
    }

    @DisplayName("Should return expected comments list.")
    @Test
    void shouldReturnExpectedCommentsList() {
        assertThat(commentRepository.getAll())
                .usingRecursiveComparison()
                .isEqualTo(getExpectedCommentsList());
    }

    @DisplayName("Should return expected comment by id.")
    @Test
    void shouldReturnExpectedCommentById() {
        assertThat(commentRepository.getById(1L))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Comment(1L, "Pulp fiction", "comment 1"));
    }

    @DisplayName("Should update comment properly.")
    @Test
    void shouldUpdateCommentProperly() {
        Comment comment = testEntityManager.find(Comment.class, 1L);
        comment.setText("comment 1 updated");

        commentRepository.update(comment);

        assertThat(commentRepository.getById(comment.getId()))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Comment(comment.getId(), "Pulp fiction", "comment 1 updated"));
    }

    @DisplayName("Should update comment text.")
    @Test
    void shouldUpdateCommentText() {
        String updatedText = "comment 1 updated";
        Long id = 1L;

        commentRepository.updateTextById(id, updatedText);
        testEntityManager.flush();
        testEntityManager.clear();

        assertThat(commentRepository.getById(id))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Comment(id, 1L, "Pulp fiction", updatedText));
    }

    @DisplayName("Should delete comment by id.")
    @Test
    void shouldDeleteCommentById() {
        Long id = 1L;
        assertThat(commentRepository.getById(id)).isNotNull();
        commentRepository.deleteById(id);
        assertThat(commentRepository.getById(id)).isNull();
    }

    private List<Comment> getExpectedCommentsList() {
        return of(
                new Comment(1L, "Pulp fiction", "comment 1"),
                new Comment(2L, "Pulp fiction", "comment 2"),
                new Comment(3L, "Pulp fiction", "comment 3"),
                new Comment(4L, "How to learn C++ in 24 hours", "comment 4")
        );
    }
}