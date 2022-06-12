package ru.otus.service.api;

import ru.otus.model.Question;

import java.util.List;

public interface AnswerValidator {

    boolean isValidAnswer(Question question, String answer);

    boolean isValidAnswerOptions(Question question, List<Integer> chosenOptions);
}
