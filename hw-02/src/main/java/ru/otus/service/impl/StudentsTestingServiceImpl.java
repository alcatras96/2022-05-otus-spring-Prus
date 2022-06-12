package ru.otus.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Question;
import ru.otus.service.api.AnswerValidator;
import ru.otus.service.api.ChosenOptionsByUserParser;
import ru.otus.service.api.StudentExamDecisionManager;
import ru.otus.service.api.StudentsTestingService;
import ru.otus.service.io.api.OutputService;
import ru.otus.service.io.api.QuestionsReader;
import ru.otus.service.io.api.UserInputService;

import static java.lang.Math.toIntExact;

@Service
@AllArgsConstructor
public class StudentsTestingServiceImpl implements StudentsTestingService {

    private final OutputService<Question> questionsOutputService;
    private final QuestionsReader questionsReader;
    private final UserInputService userInputService;
    private final ChosenOptionsByUserParser chosenOptionsByUserParser;
    private final AnswerValidator answerValidator;
    private final StudentExamDecisionManager studentExamDecisionManager;

    @Override
    public void test() {
        var questions = questionsReader.read();

        var correctlyAnsweredQuestionsCount = questions.stream()
                .peek(questionsOutputService::output)
                .filter(this::askUserInputAndValidateAnswers)
                .count();

        studentExamDecisionManager.decide(toIntExact(correctlyAnsweredQuestionsCount));
    }

    private boolean askUserInputAndValidateAnswers(Question question) {
        boolean isValidAnswers;

        if (question.withoutAnswerOptions()) {
            var userAnswer = userInputService.readStringWithPrompt("Please write your answer.");
            isValidAnswers = answerValidator.isValidAnswer(question, userAnswer);
        } else {
            var userAnswer = userInputService.readStringWithPrompt("Please write the correct options, separating them with a space.");
            var chosenOptionsByUser = chosenOptionsByUserParser.parse(userAnswer);
            isValidAnswers = answerValidator.isValidAnswerOptions(question, chosenOptionsByUser);
        }

        return isValidAnswers;
    }
}
