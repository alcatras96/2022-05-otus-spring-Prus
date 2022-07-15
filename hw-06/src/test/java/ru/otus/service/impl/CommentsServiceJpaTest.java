package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import ru.otus.model.Book;
import ru.otus.model.Comment;
import ru.otus.repository.api.CommentRepository;
import ru.otus.repository.impl.BookRepositoryJpa;
import ru.otus.repository.impl.CommentRepositoryJpa;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@DataJpaTest
@Import({CommentsServiceJpa.class, CommentRepositoryJpa.class, BookRepositoryJpa.class})
class CommentsServiceJpaTest {

    @Autowired
    private CommentsServiceJpa commentsService;

    @SpyBean
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Should save comment correctly.")
    @Test
    void shouldSaveCommentCorrectly() {
        Comment comment = new Comment(testEntityManager.getEntityManager().getReference(Book.class, 1L), "comment text");

        commentsService.save(comment);
        verify(commentRepository).insert(comment);
        reset(commentRepository);

        comment.setId(1L);
        commentsService.save(comment);
        verify(commentRepository).update(comment);
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