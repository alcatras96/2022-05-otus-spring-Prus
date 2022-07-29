package ru.otus.repository.api;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.model.Book;

import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findByName(String name);
}
