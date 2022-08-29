package ru.otus.repository.api;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.model.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String>, AuthorRepositoryCustom {

    Mono<Author> findByFullName(String name);
}
