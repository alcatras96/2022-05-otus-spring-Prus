package ru.otus.view.converter.api;

import java.util.List;
import java.util.stream.Collectors;

public interface ViewConverter<T> {

    String convert(T object);

    default String convert(List<T> objects, String delimiter) {
        return objects
                .stream()
                .map(this::convert)
                .collect(Collectors.joining(delimiter));
    }
}
