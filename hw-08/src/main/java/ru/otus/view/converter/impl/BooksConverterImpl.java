package ru.otus.view.converter.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Comment;
import ru.otus.model.Genre;
import ru.otus.view.converter.api.BookConverter;
import ru.otus.view.converter.api.ViewConverter;

@Service
@RequiredArgsConstructor
public class BooksConverterImpl implements BookConverter {

    private final ViewConverter<Genre> genreViewConverter;
    private final ViewConverter<Author> authorViewConverter;
    private final ViewConverter<Comment> commentViewConverter;

    @Override
    public String convert(Book book) {
        return String.format("Book id:%s, name:\"%s\"; Author: [%s]; Genres: [%s].",
                book.getId(), book.getName(), authorViewConverter.convert(book.getAuthor()),
                genreViewConverter.convert(book.getGenres(), ", "));
    }

    @Override
    public String convertWithComments(Book book) {
        return String.format("Book [%s], comments: [%s]", book.getName(), commentViewConverter.convert(book.getComments(), ","));
    }
}
