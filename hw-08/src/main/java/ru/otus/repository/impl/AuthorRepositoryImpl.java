package ru.otus.repository.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.repository.api.AuthorRepositoryCustom;
import ru.otus.repository.api.BookRepository;

@Service
public class AuthorRepositoryImpl implements AuthorRepositoryCustom {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorRepositoryImpl(@Lazy AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void deleteByIdWithRelations(String id) {
        bookRepository.deleteByAuthorId(id);
        authorRepository.deleteById(id);
    }
}
