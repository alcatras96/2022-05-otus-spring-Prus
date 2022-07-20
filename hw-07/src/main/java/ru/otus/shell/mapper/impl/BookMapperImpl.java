package ru.otus.shell.mapper.impl;

import org.springframework.stereotype.Service;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Genre;
import ru.otus.shell.mapper.api.BookMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookMapperImpl implements BookMapper {

    @Override
    public Book map(String name, Long authorId, String genresIds) {
        var genres = Arrays.stream(genresIds.split(","))
                .map(Long::valueOf)
                .map(Genre::new)
                .collect(Collectors.toList());

        return Book.builder()
                .name(name)
                .author(new Author(authorId))
                .genres(genres)
                .build();
    }
}
