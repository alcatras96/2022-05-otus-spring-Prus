package ru.otus.service.api;

import ru.otus.model.Author;

import java.util.Optional;

public interface AuthorsService extends CrudService<Author> {

    Optional<Author> getByFullName(String name);

    void updateFullNameById(String id, String fullName);
}
