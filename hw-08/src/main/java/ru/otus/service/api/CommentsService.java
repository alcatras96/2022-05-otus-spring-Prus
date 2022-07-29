package ru.otus.service.api;

import ru.otus.model.Comment;

import java.util.List;

public interface CommentsService extends CrudService<Comment> {

    void updateTextById(String id, String value);

    List<Comment> getCommentsByBookName(String id);
}
