package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Genre;
import ru.otus.repository.api.GenreRepository;
import ru.otus.service.api.GenresService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenresServiceMongo implements GenresService {

    private final GenreRepository genreRepository;

    @Override
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public Optional<Genre> getById(String id) {
        return genreRepository.findById(id);
    }

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Override
    public void updateNameById(String id, String name) {
        var genre = genreRepository.findById(id).orElseThrow();
        genre.setName(name);
        genreRepository.save(genre);
    }

    @Override
    public void deleteById(String id) {
        genreRepository.deleteByIdWithRelations(id);
    }
}
