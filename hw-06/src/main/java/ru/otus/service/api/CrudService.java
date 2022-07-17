package ru.otus.service.api;

import java.util.List;

public interface CrudService<T> {

    T save(T object);

    T getById(Long id);

    List<T> getAll();

    void deleteById(Long id);
}
