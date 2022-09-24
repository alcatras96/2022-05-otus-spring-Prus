package ru.otus.batch.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.batch.model.BookGenreRelation;
import ru.otus.batch.model.staging.*;
import ru.otus.batch.service.StagingCacheService;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Comment;
import ru.otus.model.Genre;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StagingBookToBookProcessor implements ItemProcessor<StagingBook, Book> {

    private final StagingCacheService cacheService;

    @Override
    public Book process(StagingBook stagingBook) {
        String originalBookId = stagingBook.getOriginalId();

        return Book.builder()
                .id(stagingBook.getNewId())
                .name(stagingBook.getName())
                .comments(getBookComments(originalBookId))
                .author(getAuthor(stagingBook.getAuthorId()))
                .genres(getGenres(originalBookId))
                .build();
    }

    private List<Comment> getBookComments(String originalBookId) {
        List<StagingComment> comments = cacheService.get("comments");

        return comments == null ? null : comments.stream()
                .filter(stagingComment -> stagingComment.getBookId().equals(originalBookId))
                .map(StagingComment::getText)
                .map(Comment::new)
                .collect(Collectors.toList());
    }

    private List<Genre> getGenres(String originalBookId) {
        List<BookGenreRelation> bookGenreRelations = cacheService.get("bookGenreRelations");
        List<StagingGenre> stagingGenres = cacheService.get("genres");

        var genreOriginalIds = bookGenreRelations.stream()
                .filter(bookGenreRelation -> bookGenreRelation.getBookId().equals(originalBookId))
                .map(BookGenreRelation::getGenreId)
                .collect(Collectors.toList());

        return genreOriginalIds.stream()
                .map(originalId ->
                        stagingGenres.stream()
                                .filter(stagingGenre -> stagingGenre.getOriginalId().equals(originalId))
                                .findAny()
                                .orElseThrow()
                )
                .map(stagingGenre -> new Genre(stagingGenre.getNewId(), null))
                .collect(Collectors.toList());
    }

    private Author getAuthor(String authorOriginalId) {
        List<StagingAuthor> stagingAuthors = cacheService.get("authors");

        return stagingAuthors.stream()
                .filter(stagingAuthor -> stagingAuthor.getOriginalId().equals(authorOriginalId))
                .findAny()
                .map(StagingEntity::getNewId)
                .map(id -> new Author(id, null))
                .orElseThrow();
    }
}
