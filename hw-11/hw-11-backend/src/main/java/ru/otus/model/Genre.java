package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Document(collection = "genres")
public class Genre {

    @Id
    private String id;
    private String name;

    public Genre(String name) {
        this.name = name;
    }
}
