package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.dto.GenreDto;
import ru.otus.dto.converter.api.Converter;
import ru.otus.model.Genre;
import ru.otus.repository.api.GenreRepository;
import ru.otus.service.api.GenresService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenresServiceMongo implements GenresService {

    private final GenreRepository genreRepository;
    private final Converter<Genre, GenreDto> genreDtoConverter;

    @Override
    public List<GenreDto> getAll() {
        return genreDtoConverter.convert(genreRepository.findAll());
    }
}
