package ru.otus.shell.mapper.api;

import ru.otus.model.Author;

public interface AuthorMapper {

    Author map(String fullName);
}
