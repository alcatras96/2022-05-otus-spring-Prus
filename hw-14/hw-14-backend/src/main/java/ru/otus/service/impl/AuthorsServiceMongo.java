package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.model.Author;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.service.api.AuthorsService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorsServiceMongo implements AuthorsService {

    private final AuthorRepository authorRepository;

    @Override
    public Author save(String authorName) {
        if (ObjectUtils.isEmpty(authorName)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Author name cannot be null or empty");
        }

        return authorRepository.save(new Author(authorName));
    }

    @Override
    public Optional<Author> findByFullName(String name) {
        return authorRepository.findByFullName(name);
    }
}
