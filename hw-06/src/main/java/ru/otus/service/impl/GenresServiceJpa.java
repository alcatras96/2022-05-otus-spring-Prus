package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Genre;
import ru.otus.repository.api.GenreRepository;
import ru.otus.service.api.GenresService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenresServiceJpa implements GenresService {

    private final GenreRepository genreRepository;

    @Transactional
    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == null) {
            genreRepository.insert(genre);
            return genre;
        } else {
            return genreRepository.update(genre);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Genre getById(Long id) {
        return genreRepository.getById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Genre> getAll() {
        return genreRepository.getAll();
    }

    @Transactional
    @Override
    public void updateNameById(Long id, String name) {
        genreRepository.updateNameById(id, name);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        genreRepository.deleteById(id);
    }
}
