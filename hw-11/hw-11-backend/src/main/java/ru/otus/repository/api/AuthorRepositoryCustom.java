package ru.otus.repository.api;

import reactor.core.publisher.Mono;

public interface AuthorRepositoryCustom {

    Mono<Void> deleteByIdWithRelations(String id);
}
