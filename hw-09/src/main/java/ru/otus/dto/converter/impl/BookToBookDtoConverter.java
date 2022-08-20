package ru.otus.dto.converter.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.dto.BookDto;
import ru.otus.dto.converter.api.Converter;
import ru.otus.model.Book;
import ru.otus.model.Comment;
import ru.otus.model.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookToBookDtoConverter implements Converter<Book, BookDto> {

    @Override
    public BookDto convert(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .authorName(book.getAuthorName())
                .genres(getGenreNames(book.getGenres()))
                .comments(getCommentsText(book.getComments()))
                .build();
    }

    private List<String> getCommentsText(List<Comment> comments) {
        return comments.stream().map(Comment::getText).collect(Collectors.toList());
    }

    private List<String> getGenreNames(List<Genre> genres) {
        return genres.stream().map(Genre::getName).collect(Collectors.toList());
    }
}