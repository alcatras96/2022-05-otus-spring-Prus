package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Question {

    private final String value;
    private final List<String> answers;

    public Question(String value) {
        this.value = value;
        this.answers = new ArrayList<>();
    }
}
