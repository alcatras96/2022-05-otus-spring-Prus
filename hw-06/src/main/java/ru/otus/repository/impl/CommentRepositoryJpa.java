package ru.otus.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.model.Comment;
import ru.otus.repository.api.CommentRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void updateTextById(Long id, String value) {
        Comment comment = getById(id);
        comment.setText(value);
    }

    @Override
    public void insert(Comment comment) {
        entityManager.persist(comment);
    }

    @Override
    public Comment getById(Long id) {
        return entityManager.find(Comment.class, id);
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
        entityManager.remove(getById(id));
    }
}
