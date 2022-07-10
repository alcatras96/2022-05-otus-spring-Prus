package ru.otus.view.converter.impl;

import org.springframework.stereotype.Service;
import ru.otus.model.Comment;
import ru.otus.view.converter.api.ViewConverter;

@Service
public class CommentsConverter implements ViewConverter<Comment> {

    @Override
    public String convert(Comment comment) {
        if (comment == null) {
            return "No comment found.";
        }

        return String.format("Comment id:%s, text:\"%s\" for book [%s].", comment.getId(), comment.getText(), comment.getBookName());
    }
}
