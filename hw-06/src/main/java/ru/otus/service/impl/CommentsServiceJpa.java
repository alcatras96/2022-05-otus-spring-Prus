package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Comment;
import ru.otus.repository.api.CommentRepository;
import ru.otus.service.api.CommentsService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentsServiceJpa implements CommentsService {

    private final CommentRepository commentRepository;

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

    @Override
    public Comment getById(Long id) {
        return commentRepository.getById(id);
    }

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
