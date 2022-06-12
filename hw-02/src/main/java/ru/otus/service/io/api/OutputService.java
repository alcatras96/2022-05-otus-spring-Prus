package ru.otus.service.io.api;

import java.util.List;

public interface OutputService<T> {

    void output(T object);

    default void output(List<T> objects) {
        objects.forEach(this::output);
    }
}
