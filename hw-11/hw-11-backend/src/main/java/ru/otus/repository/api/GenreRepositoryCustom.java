package ru.otus.repository.api;

import reactor.core.publisher.Mono;

public interface GenreRepositoryCustom {

    Mono<Void> deleteByIdWithRelations(String id);
}
