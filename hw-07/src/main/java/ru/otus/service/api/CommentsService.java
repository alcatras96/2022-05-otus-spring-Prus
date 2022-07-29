package ru.otus.service.api;

import ru.otus.model.Comment;

import java.util.List;

public interface CommentsService extends CrudService<Comment> {

    void updateTextById(Long id, String value);

    List<Comment> getCommentsByBookId(Long id);
}
