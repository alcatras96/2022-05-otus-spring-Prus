package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.annotation.LogExecutionTime;
import ru.otus.model.Book;
import ru.otus.model.Comment;
import ru.otus.repository.api.BookRepository;
import ru.otus.service.api.BooksService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BooksServiceMongo implements BooksService {

    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> getById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public Optional<Book> getByName(String name) {
        return bookRepository.findByName(name);
    }

    @LogExecutionTime
    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public void updateNameById(String bookId, String name) {
        var book = bookRepository.findById(bookId).orElseThrow();
        book.setName(name);
        bookRepository.save(book);
    }

    @Override
    public void addComment(String bookId, Comment comment) {
        var book = bookRepository.findById(bookId).orElseThrow();
        book.addComment(comment);
        bookRepository.save(book);
    }

    @Override
    public void deleteCommentsById(String bookId) {
        var book = bookRepository.findById(bookId).orElseThrow();
        book.setComments(Collections.emptyList());
        bookRepository.save(book);
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }
}
