package ru.otus.mongock.changelog;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import ru.otus.model.Book;
import ru.otus.model.Comment;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.repository.api.BookRepository;
import ru.otus.repository.api.GenreRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.of;

@RequiredArgsConstructor
@ChangeUnit(id = "BooksChangeUnit", order = "4")
public class BooksChangeUnit {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Execution
    public void insertBooks() {
        var firstAuthor = authorRepository.findByFullName("author 1").block();

        var books = of(
                Book.builder()
                        .name("book 1")
                        .authorId(firstAuthor.getId())
                        .genreIds(of(getGenreIdByName(genreRepository, "comedy"), getGenreIdByName(genreRepository, "drama")))
                        .comments(createComments("comment 1", "comment 2", "comment 3"))
                        .build(),
                Book.builder()
                        .name("book 2")
                        .authorId(firstAuthor.getId())
                        .genreIds(of(getGenreIdByName(genreRepository, "drama")))
                        .comments(createComments("comment 4", "comment 5"))
                        .build(),
                Book.builder()
                        .name("book 3")
                        .authorId(authorRepository.findByFullName("author 2").block().getId())
                        .genreIds(of(getGenreIdByName(genreRepository, "action")))
                        .comments(createComments("comment 6"))
                        .build()
        );

        bookRepository.saveAll(books).blockLast();
    }

    @RollbackExecution
    public void rollback() {
    }

    private String getGenreIdByName(GenreRepository genreRepository, String name) {
        return genreRepository.findByName(name).block().getId();
    }

    private List<Comment> createComments(String... commentText) {
        return Arrays.stream(commentText).map(Comment::new).collect(Collectors.toList());
    }
}
