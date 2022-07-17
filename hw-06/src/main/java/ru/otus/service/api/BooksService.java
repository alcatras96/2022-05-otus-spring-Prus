package ru.otus.service.api;

import ru.otus.model.Book;

public interface BooksService extends CrudService<Book> {

    void updateName(Long id, String name);
}
