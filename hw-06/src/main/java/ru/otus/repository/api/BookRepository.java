package ru.otus.repository.api;

import ru.otus.model.Book;

public interface BookRepository extends Repository<Book> {

    void updateName(Long id, String name);
}
