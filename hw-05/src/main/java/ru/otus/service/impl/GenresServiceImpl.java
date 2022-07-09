package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.api.GenreDao;
import ru.otus.domain.Genre;
import ru.otus.service.api.GenresService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GenresServiceImpl implements GenresService {

    private final GenreDao genreDao;

    @Transactional
    @Override
    public void save(Genre genre) {
        if (genre.getId() == null) {
            genreDao.insert(genre);
        } else {
            genreDao.update(genre);
        }
    }

    @Override
    public Genre getById(Long id) {
        return genreDao.getById(id);
    }

    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    @Transactional
    @Override
    public void updateNameById(Long id, String name) {
        genreDao.updateNameById(id, name);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        genreDao.deleteById(id);
    }
}
