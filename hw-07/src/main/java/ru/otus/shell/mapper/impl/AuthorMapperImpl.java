package ru.otus.shell.mapper.impl;

import org.springframework.stereotype.Service;
import ru.otus.model.Author;
import ru.otus.shell.mapper.api.AuthorMapper;

@Service
public class AuthorMapperImpl implements AuthorMapper {

    @Override
    public Author map(String fullName) {
        return new Author(fullName);
    }
}
