package ru.otus.service.api;

import ru.otus.model.Author;

public interface AuthorsService extends CrudService<Author> {

    void updateFullNameById(Long id, String fullName);
}
