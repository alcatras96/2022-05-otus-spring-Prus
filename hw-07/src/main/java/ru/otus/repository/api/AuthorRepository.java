package ru.otus.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
