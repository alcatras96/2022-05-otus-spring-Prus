package ru.otus.service.api;

import ru.otus.domain.Genre;

public interface GenresService extends CrudService<Genre> {

    void updateNameById(Long id, String name);
}
