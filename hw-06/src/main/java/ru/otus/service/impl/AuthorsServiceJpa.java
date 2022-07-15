package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Author;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.service.api.AuthorsService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorsServiceJpa implements AuthorsService {

    private final AuthorRepository authorRepository;

    @Transactional
    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            authorRepository.insert(author);
            return author;
        } else {
            return authorRepository.update(author);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Author getById(Long id) {
        return authorRepository.getById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> getAll() {
        return authorRepository.getAll();
    }

    @Transactional
    @Override
    public void updateFullNameById(Long id, String fullName) {
        authorRepository.updateFullNameById(id, fullName);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }
}
