package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Author;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.service.api.AuthorsService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorsServiceJpa implements AuthorsService {

    private final AuthorRepository authorRepository;

    @Transactional
    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Author> getById(Long id) {
        return authorRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Transactional
    @Override
    public void updateFullNameById(Long id, String fullName) {
        var authorOptional = authorRepository.findById(id);

        authorOptional.orElseThrow().setFullName(fullName);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }
}
