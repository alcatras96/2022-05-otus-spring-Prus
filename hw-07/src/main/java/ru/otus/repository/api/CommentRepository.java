package ru.otus.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Override
    @Query("select new Comment(c.id,b.id,b.name,c.text) from Comment c join c.book b")
    List<Comment> findAll();
}
