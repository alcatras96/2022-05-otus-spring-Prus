package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "books")
public class Book {

    @Id
    private String id;

    private String name;

    private String authorId;
    private List<String> genreIds;

    @Transient
    private Author author;

    @Transient
    private List<Genre> genres = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public String getAuthorName() {
        return author.getFullName();
    }
}
