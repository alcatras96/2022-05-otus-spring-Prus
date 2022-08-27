package ru.otus.repository.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.otus.repository.api.BookRepository;
import ru.otus.repository.api.GenreRepository;
import ru.otus.repository.api.GenreRepositoryCustom;

@Service
public class GenreRepositoryImpl implements GenreRepositoryCustom {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    public GenreRepositoryImpl(@Lazy GenreRepository genreRepository, BookRepository bookRepository) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Mono<Void> deleteByIdWithRelations(String id) {
        return bookRepository.deleteByGenreId(id).and(genreRepository.deleteById(id));
    }
}
