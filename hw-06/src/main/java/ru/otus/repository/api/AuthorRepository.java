package ru.otus.repository.api;

import ru.otus.model.Author;

public interface AuthorRepository extends Repository<Author> {

    void updateFullNameById(Long id, String fullName);
}
