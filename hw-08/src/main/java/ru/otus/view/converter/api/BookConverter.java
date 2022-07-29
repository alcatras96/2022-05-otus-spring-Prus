package ru.otus.view.converter.api;

import ru.otus.model.Book;

public interface BookConverter extends ViewConverter<Book> {

    String convertWithComments(Book book);
}
