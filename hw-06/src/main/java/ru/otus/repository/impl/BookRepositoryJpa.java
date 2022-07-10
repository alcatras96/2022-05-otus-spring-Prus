package ru.otus.repository.impl;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Genre;
import ru.otus.repository.api.BookRepository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
@AllArgsConstructor
public class BookRepositoryJpa implements BookRepository {

    private final EntityManager entityManager;

    @Override
    public void insert(Book book) {
        entityManager.persist(book);
    }

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b join fetch b.author", Book.class);
        return query.getResultList();
    }

    @Override
    public Book getById(Long id) {
        return entityManager.find(Book.class, id);
    }

    @Override
    public Book getOne(Long id) {
        return entityManager.getReference(Book.class, id);
    }

    @Override
    public Book update(Book book) {
        return entityManager.merge(book);
    }

    @Override
    public void updateName(Long id, String name) {
        Query query = entityManager.createQuery("update Book b set b.name = :name where b.id = :id");
        query.setParameter("name", name);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void deleteById(Long id) {
        Query query = entityManager.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
