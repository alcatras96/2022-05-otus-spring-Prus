package ru.otus.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Document(collection = "authors")
public class Author {

    @Id
    private String id;

    private String fullName;

    public Author(String fullName) {
        this.fullName = fullName;
    }
}
