package ru.otus.dao.api;

import java.util.List;

public interface Dao<T> {

    Long insert(T object);

    T getById(Long id);

    List<T> getAll();

    void update(T object);

    void deleteById(Long id);
}
