package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Comment;
import ru.otus.repository.api.BookRepository;
import ru.otus.repository.api.CommentRepository;
import ru.otus.service.api.CommentsService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentsServiceJpa implements CommentsService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public void updateTextById(Long id, String value) {
        var commentOptional = commentRepository.findById(id);
        commentOptional.orElseThrow().setText(value);
    }

    @Transactional
    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Comment> getById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentsByBookId(Long id) {
        var book = bookRepository.findById(id);
        List<Comment> comments = Collections.emptyList();
        if (book.isPresent()) {
            comments = book.get().getComments();
            Hibernate.initialize(comments);
        }
        return comments;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
