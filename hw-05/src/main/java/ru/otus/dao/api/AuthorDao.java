package ru.otus.dao.api;

import ru.otus.domain.Author;

public interface AuthorDao extends Dao<Author> {

    void updateFullNameById(Long id, String fullName);
}
