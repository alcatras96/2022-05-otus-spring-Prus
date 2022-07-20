package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.annotation.LogExecutionTime;
import ru.otus.model.Book;
import ru.otus.repository.api.BookRepository;
import ru.otus.service.api.BooksService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BooksServiceJpa implements BooksService {

    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> getById(Long id) {
        var book = bookRepository.findById(id);
        book.ifPresent(value -> Hibernate.initialize(value.getGenres()));
        return book;
    }

    @LogExecutionTime
    @Transactional(readOnly = true)
    @Override
    public List<Book> getAll() {
        var books = bookRepository.findAll();

        books.stream()
                .findAny()
                .map(Book::getGenres)
                .ifPresent(Hibernate::initialize);

        return books;
    }

    @Transactional
    @Override
    public void updateNameById(Long id, String name) {
        var bookOptional = bookRepository.findById(id);
        bookOptional.orElseThrow().setName(name);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
