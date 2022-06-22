package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public final class Student {

    private final String name;
    private final String surname;
    private int correctlyAnsweredQuestionsCount;

    public String getFirstAndLastName(){
        return name + " " + surname;
    }
}
