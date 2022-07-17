package ru.otus.service.api;

import ru.otus.model.Comment;

import java.util.List;

public interface CommentsService extends CrudService<Comment> {

    List<Comment> getCommentsByBookId(Long id);

    void updateValue(Long id, String value);
}
