package ru.otus.shell.mapper.api;

import ru.otus.model.Book;

public interface BookMapper {

    Book map(String name, Long authorId, String genresIds);
}
