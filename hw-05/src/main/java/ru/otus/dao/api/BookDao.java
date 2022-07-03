package ru.otus.dao.api;

import ru.otus.domain.Book;

public interface BookDao extends Dao<Book> {

    void updateName(Long id, String name);
}
