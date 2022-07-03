package ru.otus.service.api;

import ru.otus.domain.Author;

public interface AuthorsService extends CrudService<Author> {

    void updateFullNameById(Long id, String fullName);
}
