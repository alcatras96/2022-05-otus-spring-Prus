package ru.otus.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Comment;
import ru.otus.model.Genre;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.repository.api.BookRepository;
import ru.otus.repository.api.GenreRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.of;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "usevalad", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertGenres", author = "usevalad")
    public void insertGenres(GenreRepository genreRepository) {
        var genres = of(new Genre("comedy"),
                new Genre("drama"),
                new Genre("action"),
                new Genre("unused genre")
        );

        genreRepository.saveAll(genres);
    }

    @ChangeSet(order = "003", id = "insertAuthors", author = "usevalad")
    public void insertAuthors(AuthorRepository authorRepository) {
        var authors = of(
                new Author("author 1"),
                new Author("author 2"),
                new Author("author 3")
        );

        authorRepository.saveAll(authors);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "usevalad")
    public void insertBooks(AuthorRepository authorRepository, BookRepository bookRepository, GenreRepository genreRepository) {
        var firstAuthor = authorRepository.findByFullName("author 1").orElseThrow();

        var books = of(
                Book.builder()
                        .name("book 1")
                        .author(firstAuthor)
                        .genres(of(getGenreByName(genreRepository, "comedy"), getGenreByName(genreRepository, "drama")))
                        .comments(createComments("comment 1", "comment 2", "comment 3"))
                        .build(),
                Book.builder()
                        .name("book 2")
                        .author(firstAuthor)
                        .genres(of(getGenreByName(genreRepository, "drama")))
                        .comments(createComments("comment 4", "comment 5"))
                        .build(),
                Book.builder()
                        .name("book 3")
                        .author(authorRepository.findByFullName("author 2").orElseThrow())
                        .genres(of(getGenreByName(genreRepository, "action")))
                        .comments(createComments("comment 6"))
                        .build()
        );

        bookRepository.saveAll(books);
    }

    private Genre getGenreByName(GenreRepository genreRepository, String name) {
        return genreRepository.findByName(name).orElseThrow();
    }

    private List<Comment> createComments(String... commentText) {
        return Arrays.stream(commentText).map(Comment::new).collect(Collectors.toList());
    }
}
