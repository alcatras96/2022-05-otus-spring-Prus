package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dto.GenreDto;
import ru.otus.service.api.GenresService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GenreController {

    private final GenresService genresService;

    @GetMapping("/genre")
    public List<GenreDto> getGenres() {
        return genresService.getAll();
    }
}
