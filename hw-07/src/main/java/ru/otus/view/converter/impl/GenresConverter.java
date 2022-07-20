package ru.otus.view.converter.impl;

import org.springframework.stereotype.Service;
import ru.otus.model.Genre;
import ru.otus.view.converter.api.ViewConverter;

@Service
public class GenresConverter implements ViewConverter<Genre> {

    @Override
    public String convert(Genre genre) {
        return String.format("Id:%s, name:\"%s\"", genre.getId(), genre.getName());
    }
}
