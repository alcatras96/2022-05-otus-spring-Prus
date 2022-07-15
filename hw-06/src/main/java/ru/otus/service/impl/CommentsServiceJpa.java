package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Book;
import ru.otus.model.Comment;
import ru.otus.repository.api.BookRepository;
import ru.otus.repository.api.CommentRepository;
import ru.otus.service.api.CommentsService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsServiceJpa implements CommentsService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public void updateValue(Long id, String value) {
        commentRepository.updateTextById(id, value);
    }

    @Transactional
    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            commentRepository.insert(comment);
            return comment;
        } else {
            return commentRepository.update(comment);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentsByBookId(Long id) {
        Book book = bookRepository.getById(id);
        List<Comment> comments = Collections.emptyList();
        if (book != null) {
            comments = book.getComments();
            Hibernate.initialize(comments);
        }
        return comments;
    }

    @Transactional(readOnly = true)
    @Override
    public Comment getById(Long id) {
        return commentRepository.getById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getAll() {
        return commentRepository.getAll();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
