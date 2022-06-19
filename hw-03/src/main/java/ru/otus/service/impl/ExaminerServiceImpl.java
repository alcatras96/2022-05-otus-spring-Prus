package ru.otus.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Question;
import ru.otus.model.Student;
import ru.otus.service.api.AnswerValidator;
import ru.otus.service.api.ExaminerService;
import ru.otus.service.api.I18nService;
import ru.otus.service.api.StudentExamDecisionManager;
import ru.otus.service.io.api.OutputService;
import ru.otus.service.io.api.UserInputService;

import java.util.List;

import static java.lang.Math.toIntExact;

@Service
@AllArgsConstructor
public class ExaminerServiceImpl implements ExaminerService {

    private final OutputService<Question> questionsOutputService;
    private final UserInputService userInputService;
    private final AnswerValidator answerValidator;
    private final StudentExamDecisionManager studentExamDecisionManager;
    private final I18nService i18nService;

    @Override
    public void ask(Student student, List<Question> questions) {
        var correctlyAnsweredQuestionsCount = questions.stream()
                .filter(this::askAndValidateAnswer)
                .count();

        student.setCorrectlyAnsweredQuestionsCount(toIntExact(correctlyAnsweredQuestionsCount));
    }

    @Override
    public void decide(Student student) {
        studentExamDecisionManager.decide(student);
    }

    private boolean askAndValidateAnswer(Question question) {
        questionsOutputService.output(question);

        var userAnswer = askStudentForAnswer(question);
        return answerValidator.isValidAnswer(question, userAnswer);
    }

    private String askStudentForAnswer(Question question) {
        var messageKey = question.withoutAnswerOptions() ? "write.answer" : "write.correct.options";

        return userInputService.readStringWithPrompt(i18nService.getMessage(messageKey));
    }
}
