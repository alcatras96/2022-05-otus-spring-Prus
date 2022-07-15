package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.annotation.LogExecutionTime;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Genre;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.repository.api.BookRepository;
import ru.otus.repository.api.GenreRepository;
import ru.otus.service.api.BooksService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class BooksServiceJpa implements BooksService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            setProxyRelations(book);
            bookRepository.insert(book);
            return book;
        } else {
            return bookRepository.update(book);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Book getById(Long id) {
        Book book = bookRepository.getById(id);
        if (book != null) {
            Hibernate.initialize(book.getGenres());
        }
        return book;
    }

    @Transactional(readOnly = true)
    @LogExecutionTime
    @Override
    public List<Book> getAll() {
        List<Book> books = bookRepository.getAll();

        books.stream()
                .findAny()
                .map(Book::getGenres)
                .ifPresent(Hibernate::initialize);

        return books;
    }

    @Transactional
    @Override
    public void updateName(Long id, String name) {
        bookRepository.updateName(id, name);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    private void setProxyRelations(Book book) {
        List<Genre> genres = ofNullable(book.getGenres())
                .map(Collection::stream)
                .map(genreStream ->
                        genreStream
                                .map(genre -> genreRepository.getOne(genre.getId()))
                                .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());

        Author author = ofNullable(book.getAuthor())
                .map(Author::getId)
                .map(authorRepository::getOne)
                .orElse(null);

        book.setGenres(genres);
        book.setAuthor(author);
    }
}
