package ru.otus.service.api;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {

    T save(T object);

    Optional<T> getById(String id);

    List<T> getAll();

    void deleteById(String id);
}
