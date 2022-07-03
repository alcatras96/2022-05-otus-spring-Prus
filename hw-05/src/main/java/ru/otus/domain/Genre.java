package ru.otus.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Genre {

    private Long id;
    private String name;

    public Genre(Long id) {
        this.id = id;
    }

    public Genre(String name) {
        this.name = name;
    }
}
