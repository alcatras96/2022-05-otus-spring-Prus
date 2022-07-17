package ru.otus.service.api;

import ru.otus.model.Book;

public interface BooksService extends CrudService<Book> {

    void updateNameById(Long id, String name);
}
