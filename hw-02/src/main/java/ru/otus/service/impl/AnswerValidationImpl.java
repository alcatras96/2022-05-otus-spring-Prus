package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Question;
import ru.otus.service.api.AnswerValidator;
import ru.otus.service.api.ChosenOptionsByUserParser;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerValidationImpl implements AnswerValidator {

    private final ChosenOptionsByUserParser chosenOptionsByUserParser;

    @Override
    public boolean isValidAnswer(Question question, String answer) {
        boolean isValidAnswer;

        if (question.withoutAnswerOptions()) {
            isValidAnswer = question.getFirstAnswerValue().equalsIgnoreCase(answer);
        } else {
            var chosenOptionsByUser = chosenOptionsByUserParser.parse(answer);
            isValidAnswer = isValidAnswerOptions(question, chosenOptionsByUser);
        }

        return isValidAnswer;
    }

    private boolean isValidAnswerOptions(Question question, List<Integer> chosenOptions) {
        try {
            return question.getAnswersByIndices(chosenOptions).containsAll(question.getCorrectAnswers());
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }
}
