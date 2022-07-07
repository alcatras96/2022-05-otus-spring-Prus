package ru.otus.service.api;

import ru.otus.domain.Book;

import java.util.List;

public interface BooksService extends CrudService<Book> {

    void updateName(Long id, String name);

    List<Book> getAllInOneQuery();

    void saveAll(List<Book> books);
}
