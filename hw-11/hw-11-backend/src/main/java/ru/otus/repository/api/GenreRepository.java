package ru.otus.repository.api;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.model.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String>, GenreRepositoryCustom {

    Mono<Genre> findByName(String name);
}
