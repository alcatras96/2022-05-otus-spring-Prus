package ru.otus.service.api;

import ru.otus.model.Question;

public interface AnswerValidator {

    boolean isValidAnswer(Question question, String answer);
}
