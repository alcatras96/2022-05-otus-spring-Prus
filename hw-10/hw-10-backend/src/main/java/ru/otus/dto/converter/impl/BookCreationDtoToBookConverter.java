package ru.otus.dto.converter.impl;

import org.springframework.stereotype.Service;
import ru.otus.dto.BookCreationDto;
import ru.otus.dto.converter.api.Converter;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookCreationDtoToBookConverter implements Converter<BookCreationDto, Book> {

    @Override
    public Book convert(BookCreationDto bookCreationDto) {
        return Book.builder()
                .name(bookCreationDto.getName())
                .author(getAuthor(bookCreationDto))
                .genres(getGenres(bookCreationDto))
                .build();
    }

    private Author getAuthor(BookCreationDto bookCreationDto) {
        return new Author(bookCreationDto.getAuthorName());
    }

    private List<Genre> getGenres(BookCreationDto bookCreationDto) {
        return bookCreationDto.getGenreIds()
                .stream()
                .map(id -> new Genre(id, null))
                .collect(Collectors.toList());
    }
}
