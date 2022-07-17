package ru.otus.repository.api;

import ru.otus.model.Comment;

public interface CommentRepository extends Repository<Comment> {

    void updateTextById(Long id, String value);
}
