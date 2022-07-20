package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Genre;
import ru.otus.repository.api.GenreRepository;
import ru.otus.service.api.GenresService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenresServiceJpa implements GenresService {

    private final GenreRepository genreRepository;

    @Override
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public Optional<Genre> getById(Long id) {
        return genreRepository.findById(id);
    }

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Transactional
    @Override
    public void updateNameById(Long id, String name) {
        var genreOptional = genreRepository.findById(id);
        genreOptional.orElseThrow().setName(name);
    }

    @Override
    public void deleteById(Long id) {
        genreRepository.deleteById(id);
    }
}
