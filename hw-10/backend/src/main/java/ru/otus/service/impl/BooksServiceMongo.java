package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.annotation.LogExecutionTime;
import ru.otus.controller.exception.NotFoundException;
import ru.otus.dto.BookCreationDto;
import ru.otus.dto.BookDto;
import ru.otus.dto.converter.api.Converter;
import ru.otus.model.Book;
import ru.otus.model.Comment;
import ru.otus.repository.api.BookRepository;
import ru.otus.service.api.AuthorsService;
import ru.otus.service.api.BooksService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BooksServiceMongo implements BooksService {

    private final BookRepository bookRepository;
    private final AuthorsService authorsService;
    private final Converter<Book, BookDto> bookDtoConverter;
    private final Converter<BookCreationDto, Book> bookCreationDtoConverter;

    @Override
    public void save(BookCreationDto bookCreationDto) {
        var book = bookCreationDtoConverter.convert(bookCreationDto);
        var authorName = bookCreationDto.getAuthorName();
        var authorOptional = authorsService.findByFullName(authorName);
        authorOptional.ifPresentOrElse(book::setAuthor, () -> book.setAuthor(authorsService.save(authorName)));
        bookRepository.save(book);
    }

    @Override
    public Optional<BookDto> getById(String id) {
        return bookRepository.findById(id).map(bookDtoConverter::convert);
    }

    @Override
    public Optional<BookDto> getByName(String name) {
        return bookRepository.findByName(name).map(bookDtoConverter::convert);
    }

    @LogExecutionTime
    @Override
    public List<BookDto> getAll() {
        return bookDtoConverter.convert(bookRepository.findAll());
    }

    @Override
    public void updateName(String id, String name) {
        var book = bookRepository.findById(id).orElseThrow(NotFoundException::new);
        book.setName(name);
        bookRepository.save(book);
    }

    @Override
    public void addComment(String bookId, String commentText) {
        var book = bookRepository.findById(bookId).orElseThrow();
        book.addComment(new Comment(commentText));
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
