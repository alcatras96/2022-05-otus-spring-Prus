package ru.otus.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.model.Question;
import ru.otus.service.api.AnswerValidator;

import java.util.List;

@Service
public class AnswerValidationImpl implements AnswerValidator {

    @Override
    public boolean isValidAnswer(Question question, String answer) {
        return question.getFirstAnswerValue().equalsIgnoreCase(answer);
    }

    @Override
    public boolean isValidAnswerOptions(Question question, List<Integer> chosenOptions) {
        try {
            return question.getAnswersByIndices(chosenOptions).containsAll(question.getCorrectAnswers());
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }
}
