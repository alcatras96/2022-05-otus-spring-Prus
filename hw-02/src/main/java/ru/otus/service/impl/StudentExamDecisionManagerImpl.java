package ru.otus.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.model.Student;
import ru.otus.service.api.StudentExamDecisionManager;
import ru.otus.service.io.api.OutputService;

@Service
public class StudentExamDecisionManagerImpl implements StudentExamDecisionManager {

    private final OutputService<String> outputService;
    private final int minimumNumberOfCorrectAnswers;

    public StudentExamDecisionManagerImpl(OutputService<String> outputService,
                                          @Value("${questions.minimum-number-of-correct-answers}") int minimumNumberOfCorrectAnswers) {
        this.outputService = outputService;
        this.minimumNumberOfCorrectAnswers = minimumNumberOfCorrectAnswers;
    }

    @Override
    public void decide(Student student) {
        int correctlyAnsweredQuestionsCount = student.getCorrectlyAnsweredQuestionsCount();
        outputService.output(String.format("You answered %s questions correctly.", correctlyAnsweredQuestionsCount));
        outputService.output(correctlyAnsweredQuestionsCount < minimumNumberOfCorrectAnswers ?
                "You didn't pass the exam" : "You passed the exam");
    }
}
