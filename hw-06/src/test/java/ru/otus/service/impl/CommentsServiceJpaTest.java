package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.model.Comment;
import ru.otus.repository.api.CommentRepository;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = CommentsServiceJpa.class)
class CommentsServiceJpaTest {

    @Autowired
    private CommentsServiceJpa commentsService;

    @MockBean
    private CommentRepository commentRepository;

    @DisplayName("Should save comment correctly.")
    @Test
    void shouldSaveCommentCorrectly() {
        Comment comment = new Comment();

        commentsService.save(comment);
        verify(commentRepository).insert(comment);
        reset(commentRepository);

        comment.setId(5L);
        commentsService.save(comment);
        verify(commentRepository).update(comment);
    }
}