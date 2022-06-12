package ru.otus.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Question;
import ru.otus.service.api.AnswerValidator;
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
    private final AnswerValidator answerValidator;
    private final StudentExamDecisionManager studentExamDecisionManager;

    @Override
    public void test() {
        var questions = questionsReader.read();

        var correctlyAnsweredQuestionsCount = questions.stream()
                .peek(questionsOutputService::output)
                .filter(this::askAndValidateUserAnswer)
                .count();

        studentExamDecisionManager.decide(toIntExact(correctlyAnsweredQuestionsCount));
    }

    private boolean askAndValidateUserAnswer(Question question) {
        String userAnswer = askUserAnswer(question);
        return answerValidator.isValidAnswer(question, userAnswer);
    }

    private String askUserAnswer(Question question) {
        return userInputService.readStringWithPrompt(question.withoutAnswerOptions()
                ? "Please write your answer." : "Please write the correct options, separating them with a space.");
    }
}
