package ru.otus.shell.mapper.api;

import ru.otus.model.Comment;

public interface CommentMapper {

    Comment map(String value, Long bookId);
}
