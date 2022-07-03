package ru.otus.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Book {

    private Long id;
    private String name;
    private Long authorId;

    private List<Genre> genres;

    public Book(Long id, String name, Long authorId) {
        this.id = id;
        this.name = name;
        this.authorId = authorId;
    }

    public Book(String name, Long authorId) {
        this.name = name;
        this.authorId = authorId;
    }
}
