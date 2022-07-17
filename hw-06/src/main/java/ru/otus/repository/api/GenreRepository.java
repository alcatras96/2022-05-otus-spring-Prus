package ru.otus.repository.api;

import ru.otus.model.Genre;

public interface GenreRepository extends Repository<Genre> {

    void updateNameById(Long id, String name);
}
