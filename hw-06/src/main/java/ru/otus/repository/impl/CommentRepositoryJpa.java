package ru.otus.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Comment;
import ru.otus.repository.api.CommentRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {

    private final EntityManager entityManager;

    @Override
    public void updateTextById(Long id, String value) {
        Query query = entityManager.createQuery("update Comment c set c.text = :value where c.id = :id");
        query.setParameter("value", value);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void insert(Comment comment) {
        entityManager.persist(comment);
    }

    @Override
    public Comment getById(Long id) {
        try {
            TypedQuery<Comment> query = entityManager.createQuery("select new Comment(c.id,b.id,b.name,c.text) " +
                    "from Comment c join c.book b where c.id = :id", Comment.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Comment getOne(Long id) {
        return entityManager.getReference(Comment.class, id);
    }

    @Override
    public List<Comment> getAll() {
        TypedQuery<Comment> query = entityManager.createQuery("select new Comment(c.id,b.name,c.text) " +
                "from Comment c join c.book b", Comment.class);
        return query.getResultList();
    }

    @Override
    public Comment update(Comment comment) {
        return entityManager.merge(comment);
    }

    @Override
    public void deleteById(Long id) {
        Query query = entityManager.createQuery("delete from Comment c where c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
