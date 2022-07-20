package ru.otus.view.converter.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Genre;
import ru.otus.view.converter.api.ViewConverter;

@Service
@RequiredArgsConstructor
public class BooksConverter implements ViewConverter<Book> {

    private final ViewConverter<Genre> genreViewConverter;
    private final ViewConverter<Author> authorViewConverter;

    @Override
    public String convert(Book book) {
        return String.format("Book id:%s, name:\"%s\"; Author: [%s]; Genres: [%s].",
                book.getId(), book.getName(), authorViewConverter.convert(book.getAuthor()),
                genreViewConverter.convert(book.getGenres(), ", "));
    }
}
