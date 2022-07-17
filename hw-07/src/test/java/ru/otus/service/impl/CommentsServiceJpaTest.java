package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.model.Comment;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CommentsServiceJpa.class)
class CommentsServiceJpaTest {

    @Autowired
    private CommentsServiceJpa commentsService;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Should update comment text.")
    @Test
    void shouldUpdateCommentText() {
        String updatedText = "comment 1 updated";
        Long id = 1L;

        commentsService.updateTextById(id, updatedText);
        testEntityManager.flush();
        testEntityManager.clear();

        assertThat(commentsService.getById(id))
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Comment(id, 1L, "Pulp fiction", updatedText));
    }

    @DisplayName("Should return expected comments by book id.")
    @Test
    void shouldReturnExpectedCommentsByBookId() {
        assertThat(commentsService.getCommentsByBookId(1L))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getFirstBookComments());
    }

    private List<Comment> getFirstBookComments() {
        return of(
                new Comment(1L, 1L, "Pulp fiction", "comment 1"),
                new Comment(2L, 1L, "Pulp fiction", "comment 2"),
                new Comment(3L, 1L, "Pulp fiction", "comment 3")
        );
    }
}