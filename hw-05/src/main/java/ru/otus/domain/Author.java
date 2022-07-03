package ru.otus.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Author {

    private Long id;
    private String fullName;

    public Author(String fullName) {
        this.fullName = fullName;
    }
}
