package ru.otus.repository.api;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.model.Author;

import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String> {

    Optional<Author> findByFullName(String name);
}
