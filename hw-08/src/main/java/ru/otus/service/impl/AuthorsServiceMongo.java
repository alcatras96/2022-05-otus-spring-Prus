package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Author;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.service.api.AuthorsService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorsServiceMongo implements AuthorsService {

    private final AuthorRepository authorRepository;

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Optional<Author> getById(String id) {
        return authorRepository.findById(id);
    }

    @Override
    public Optional<Author> getByFullName(String name) {
        return authorRepository.findByFullName(name);
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public void updateFullNameById(String id, String fullName) {
        var author = authorRepository.findById(id).orElseThrow();
        author.setFullName(fullName);
        authorRepository.save(author);
    }

    @Override
    public void deleteById(String id) {
        authorRepository.deleteById(id);
    }
}
