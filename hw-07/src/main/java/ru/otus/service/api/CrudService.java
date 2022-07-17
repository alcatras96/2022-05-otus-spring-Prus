package ru.otus.service.api;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {

    T save(T object);

    Optional<T> getById(Long id);

    List<T> getAll();

    void deleteById(Long id);
}
