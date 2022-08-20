package ru.otus.service.api;

import ru.otus.dto.BookCreationDto;
import ru.otus.dto.BookDto;
import ru.otus.dto.BookUpdateDto;
import ru.otus.dto.CommentCreationDto;
import ru.otus.model.Comment;

import java.util.List;
import java.util.Optional;

public interface BooksService {

    Optional<BookDto> getById(String id);

    List<BookDto> getAll();

    Optional<BookDto> getByName(String name);

    void save(BookCreationDto object);

    void updateName(BookUpdateDto bookUpdateDto);

    void addComment(CommentCreationDto commentCreationDto);

    void deleteById(String id);

    void deleteCommentsById(String bookId);
}
