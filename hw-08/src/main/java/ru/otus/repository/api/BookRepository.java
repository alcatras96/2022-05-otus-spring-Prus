package ru.otus.repository.api;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.model.Book;

import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findByName(String name);

    void deleteByAuthorId(String id);

    @DeleteQuery(value = "{ 'genres.id' : ?0 }")
    void deleteByGenreId(String genreId);
}
