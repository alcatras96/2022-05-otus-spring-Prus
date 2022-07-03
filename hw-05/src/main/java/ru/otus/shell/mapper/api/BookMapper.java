package ru.otus.shell.mapper.api;

import ru.otus.domain.Book;

public interface BookMapper {

    Book map(String name, Long authorId, String genresIds);
}
