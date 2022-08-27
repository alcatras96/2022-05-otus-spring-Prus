package ru.otus.mongock.changelog;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import ru.otus.model.Genre;
import ru.otus.repository.api.GenreRepository;

import static java.util.List.of;

@RequiredArgsConstructor
@ChangeUnit(id = "GenresChangeUnit", order = "2")
public class GenresChangeUnit {

    private final GenreRepository genreRepository;

    @Execution
    public void insertGenres() {
        var genres = of(new Genre("comedy"),
                new Genre("drama"),
                new Genre("action")
        );

        genreRepository.saveAll(genres).blockLast();
    }

    @RollbackExecution
    public void rollback() {
    }
}
