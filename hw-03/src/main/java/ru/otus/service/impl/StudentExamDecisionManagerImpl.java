package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.configuration.QuestionsProperties;
import ru.otus.model.Student;
import ru.otus.service.api.I18nService;
import ru.otus.service.api.StudentExamDecisionManager;
import ru.otus.service.io.api.OutputService;

import static java.lang.String.valueOf;

@Service
@RequiredArgsConstructor
public class StudentExamDecisionManagerImpl implements StudentExamDecisionManager {

    private final OutputService<String> outputService;
    private final QuestionsProperties properties;
    private final I18nService i18nService;

    @Override
    public void decide(Student student) {
        int correctlyAnsweredQuestionsCount = student.getCorrectlyAnsweredQuestionsCount();
        var examMessageKey = correctlyAnsweredQuestionsCount < properties.getMinimumNumberOfCorrectAnswers() ?
                "you.did.not.pass.exam" : "you.passed.exam";

        outputService.output(i18nService.getMessage("you.answered.questions.correctly", valueOf(correctlyAnsweredQuestionsCount)));
        outputService.output(i18nService.getMessage(examMessageKey));
    }
}
