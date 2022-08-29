package ru.otus.repository.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.model.Book;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.repository.api.BookRepository;
import ru.otus.repository.api.BookRepositoryCustom;
import ru.otus.repository.api.GenreRepository;

@Service
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    public BookRepositoryImpl(@Lazy BookRepository bookRepository,
                              @Lazy AuthorRepository authorRepository,
                              @Lazy GenreRepository genreRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Mono<Book> findByIdWithRelations(String id) {
        return bookRepository.findById(id)
                .flatMap(book ->
                        Mono.zip(
                                authorRepository.findById(book.getAuthorId()),
                                genreRepository.findAllById(book.getGenreIds()).collectList()
                        ).doOnNext(relatedEntities -> {
                            book.setAuthor(relatedEntities.getT1());
                            book.setGenres(relatedEntities.getT2());
                        }).thenReturn(book)
                );
    }

    @Override
    public Flux<Book> findAllWithRelations() {
        return bookRepository.findAll()
                .flatMap(book ->
                        Mono.zip(
                                authorRepository.findById(book.getAuthorId()),
                                genreRepository.findAllById(book.getGenreIds()).collectList()
                        ).doOnNext(relatedEntities -> {
                            book.setAuthor(relatedEntities.getT1());
                            book.setGenres(relatedEntities.getT2());
                        }).thenReturn(book)
                );
    }
}
