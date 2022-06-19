package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public final class Student {

    private final String firstAndLastName;
    private int correctlyAnsweredQuestionsCount;
}
