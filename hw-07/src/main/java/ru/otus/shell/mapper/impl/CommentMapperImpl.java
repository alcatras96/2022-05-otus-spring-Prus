package ru.otus.shell.mapper.impl;

import org.springframework.stereotype.Service;
import ru.otus.model.Book;
import ru.otus.model.Comment;
import ru.otus.shell.mapper.api.CommentMapper;

@Service
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment map(String value, Long bookId) {
        return new Comment(new Book(bookId), value);
    }
}
