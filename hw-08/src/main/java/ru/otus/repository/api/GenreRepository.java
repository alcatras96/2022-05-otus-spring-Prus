package ru.otus.repository.api;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.model.Genre;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String> {

    Optional<Genre> findByName(String name);
}
