package ru.otus.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.model.Genre;
import ru.otus.repository.api.GenreRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void insert(Genre genre) {
        entityManager.persist(genre);
    }

    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> query = entityManager.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Genre getById(Long id) {
        return entityManager.find(Genre.class, id);
    }

    @Override
    public Genre getOne(Long id) {
        return entityManager.getReference(Genre.class, id);
    }

    @Override
    public Genre update(Genre genre) {
        return entityManager.merge(genre);
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(getById(id));
    }

    @Override
    public void updateNameById(Long id, String name) {
        Genre genre = getById(id);
        genre.setName(name);
    }
}
