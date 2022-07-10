package ru.otus.shell.mapper.impl;

import org.springframework.stereotype.Service;
import ru.otus.model.Genre;
import ru.otus.shell.mapper.api.GenreMapper;

@Service
public class GenreMapperImpl implements GenreMapper {

    @Override
    public Genre map(String name) {
        return new Genre(name);
    }
}
