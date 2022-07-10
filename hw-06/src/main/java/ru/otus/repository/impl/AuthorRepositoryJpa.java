package ru.otus.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Author;
import ru.otus.repository.api.AuthorRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorRepositoryJpa implements AuthorRepository {

    private final EntityManager entityManager;

    @Override
    public void insert(Author author) {
        entityManager.persist(author);
    }

    @Override
    public List<Author> getAll() {
        TypedQuery<Author> query = entityManager.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Author getById(Long id) {
        return entityManager.find(Author.class, id);
    }

    @Override
    public Author getOne(Long id) {
        return entityManager.getReference(Author.class, id);
    }

    @Override
    public Author update(Author author) {
        return entityManager.merge(author);
    }

    @Override
    public void updateFullNameById(Long id, String fullName) {
        Query query = entityManager.createQuery("update Author a " +
                "set a.fullName = :fullName " +
                "where a.id = :id");
        query.setParameter("fullName", fullName);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void deleteById(Long id) {
        Query query = entityManager.createQuery("delete from Author a where a.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
