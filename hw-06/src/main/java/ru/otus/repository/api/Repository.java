package ru.otus.repository.api;

import java.util.List;

public interface Repository<T> {

    void insert(T object);

    T getById(Long id);

    T getOne(Long id);

    List<T> getAll();

    T update(T object);

    void deleteById(Long id);
}
