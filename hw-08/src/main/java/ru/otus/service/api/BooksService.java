package ru.otus.service.api;

import ru.otus.model.Book;
import ru.otus.model.Comment;

import java.util.Optional;

public interface BooksService extends CrudService<Book> {

    Optional<Book> getByName(String name);

    void updateNameById(String bookId, String name);

    void addComment(String bookId, Comment comment);

    void deleteCommentsById(String bookId);
}
