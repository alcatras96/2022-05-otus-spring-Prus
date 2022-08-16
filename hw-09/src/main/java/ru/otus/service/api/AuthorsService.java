package ru.otus.service.api;

import ru.otus.model.Author;

import java.util.Optional;

public interface AuthorsService {

    Author save(String authorName);

    Optional<Author> findByFullName(String name);
}
