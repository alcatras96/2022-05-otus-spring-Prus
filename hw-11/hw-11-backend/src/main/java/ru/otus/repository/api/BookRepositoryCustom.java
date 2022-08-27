package ru.otus.repository.api;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.model.Book;

public interface BookRepositoryCustom {

    Mono<Book> findByIdWithRelations(String id);

    Flux<Book> findAllWithRelations();
}
