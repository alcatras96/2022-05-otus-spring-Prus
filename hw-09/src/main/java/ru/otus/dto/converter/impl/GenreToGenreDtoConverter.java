package ru.otus.dto.converter.impl;

import org.springframework.stereotype.Service;
import ru.otus.dto.GenreDto;
import ru.otus.dto.converter.api.Converter;
import ru.otus.model.Genre;

@Service
public class GenreToGenreDtoConverter implements Converter<Genre, GenreDto> {

    @Override
    public GenreDto convert(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
