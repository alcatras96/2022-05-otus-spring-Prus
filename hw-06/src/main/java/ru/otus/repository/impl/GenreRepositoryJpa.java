package ru.otus.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Genre;
import ru.otus.repository.api.GenreRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreRepositoryJpa implements GenreRepository {

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
        Query query = entityManager.createQuery("delete from Genre g where g.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void updateNameById(Long id, String name) {
        Query query = entityManager.createQuery("update Genre g set g.name = :name where g.id = :id");
        query.setParameter("name", name);
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
