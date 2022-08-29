package ru.otus.repository.api;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.model.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String>, BookRepositoryCustom {

    Mono<Book> findByName(String name);

    Mono<Void> deleteByAuthorId(String id);

    @DeleteQuery(value = "{ 'genreIds' : ?0 }")
    Mono<Void> deleteByGenreId(String genreId);
}
