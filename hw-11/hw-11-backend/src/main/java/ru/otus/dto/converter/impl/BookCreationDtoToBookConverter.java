package ru.otus.dto.converter.impl;

import org.springframework.stereotype.Service;
import ru.otus.dto.BookCreationDto;
import ru.otus.dto.converter.api.Converter;
import ru.otus.model.Author;
import ru.otus.model.Book;

@Service
public class BookCreationDtoToBookConverter implements Converter<BookCreationDto, Book> {

    @Override
    public Book convert(BookCreationDto bookCreationDto) {
        return Book.builder()
                .name(bookCreationDto.getName())
                .author(getAuthor(bookCreationDto))
                .genreIds(bookCreationDto.getGenreIds())
                .build();
    }

    private Author getAuthor(BookCreationDto bookCreationDto) {
        return new Author(bookCreationDto.getAuthorName());
    }
}
