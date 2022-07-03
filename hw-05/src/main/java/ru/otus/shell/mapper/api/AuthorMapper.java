package ru.otus.shell.mapper.api;

import ru.otus.domain.Author;

public interface AuthorMapper {

    Author map(String fullName);
}
