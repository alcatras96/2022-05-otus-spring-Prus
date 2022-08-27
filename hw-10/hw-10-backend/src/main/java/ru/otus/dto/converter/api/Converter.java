package ru.otus.dto.converter.api;

import java.util.List;
import java.util.stream.Collectors;

public interface Converter<IN, OUT> {

    OUT convert(IN object);

    default List<OUT> convert(List<IN> objects) {
        return objects
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
