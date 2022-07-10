package ru.otus.service.api;

import ru.otus.model.Comment;

public interface CommentsService extends CrudService<Comment> {

    void updateValue(Long id, String value);
}
