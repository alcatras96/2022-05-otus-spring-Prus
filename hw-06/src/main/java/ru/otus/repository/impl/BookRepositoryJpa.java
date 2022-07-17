package ru.otus.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.model.Book;
import ru.otus.repository.api.BookRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
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
        Book book = getById(id);
        book.setName(name);
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(getById(id));
    }
}
