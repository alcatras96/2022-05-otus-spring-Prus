package ru.otus.shell.mapper.impl;

import org.springframework.stereotype.Service;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;
import ru.otus.shell.mapper.api.BookMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookMapperImpl implements BookMapper {

    @Override
    public Book map(String name, Long authorId, String genresIds) {
        Book book = new Book(name, authorId);

        List<Genre> genres = Arrays.stream(genresIds.split(","))
                .map(Long::valueOf)
                .map(Genre::new)
                .collect(Collectors.toList());
        book.setGenres(genres);

        return book;
    }
}
