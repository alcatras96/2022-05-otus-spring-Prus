package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenreDto {

    private String id;
    private String name;

    public GenreDto(String name) {
        this.name = name;
    }
}
