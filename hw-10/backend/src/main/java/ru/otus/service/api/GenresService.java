package ru.otus.service.api;

import ru.otus.dto.GenreDto;

import java.util.List;

public interface GenresService {

    List<GenreDto> getAll();
}
