package ru.otus.dao.api;

import ru.otus.domain.Book;

import java.util.List;

public interface BookDao extends Dao<Book> {

    List<Book> getAllInOneQuery();

    void updateName(Long id, String name);
}
