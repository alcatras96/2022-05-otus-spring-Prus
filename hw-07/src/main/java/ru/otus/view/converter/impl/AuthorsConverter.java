package ru.otus.view.converter.impl;

import org.springframework.stereotype.Service;
import ru.otus.model.Author;
import ru.otus.view.converter.api.ViewConverter;

@Service
public class AuthorsConverter implements ViewConverter<Author> {

    @Override
    public String convert(Author author) {
        return String.format("Id:%s, name:\"%s\"", author.getId(), author.getFullName());
    }
}
