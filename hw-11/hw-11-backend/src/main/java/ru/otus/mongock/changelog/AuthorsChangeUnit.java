package ru.otus.mongock.changelog;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import ru.otus.model.Author;
import ru.otus.model.Genre;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.repository.api.GenreRepository;

import static java.util.List.of;

@RequiredArgsConstructor
@ChangeUnit(id = "AuthorsChangeUnit", order = "3")
public class AuthorsChangeUnit {

    private final AuthorRepository authorRepository;

    @Execution
    public void insertAuthors() {
        var authors = of(
                new Author("author 1"),
                new Author("author 2"),
                new Author("author 3")
        );

        authorRepository.saveAll(authors).blockLast();
    }

    @RollbackExecution
    public void rollback() {
    }
}
