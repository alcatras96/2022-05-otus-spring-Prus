package ru.otus.service.api;

import ru.otus.dto.BookCreationDto;
import ru.otus.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BooksService {

    Optional<BookDto> getById(String id);

    List<BookDto> getAll();

    Optional<BookDto> getByName(String name);

    void save(BookCreationDto object);

    void updateName(String id, String name);

    void addComment(String bookId, String commentText);

    void deleteById(String id);

    void deleteCommentsById(String bookId);
}
