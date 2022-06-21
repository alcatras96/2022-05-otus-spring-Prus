package ru.otus.service.io.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.model.Answer;
import ru.otus.model.Question;
import ru.otus.service.io.api.OutputService;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.mockito.Mockito.*;

@SpringBootTest
class QuestionsOutputServiceTest {

    @MockBean
    OutputService<String> stringOutputService;

    @Autowired
    QuestionsOutputService questionOutputService;

    @Test
    @DisplayName("Should output all questions with answers")
    void shouldOutputAllQuestionsWithAnswers() {
        questionOutputService.output(getQuestions());
        verify(stringOutputService, times(7)).output(any(String.class));
    }

    private List<Question> getQuestions() {
        return of(
                new Question("How is OOP stands for?", singletonList(new Answer("Object Oriented Programming", true))),
                new Question("What development principles exist?",
                        of(new Answer("SOFT", false),
                                new Answer("GRASP", true),
                                new Answer("KISS", true),
                                new Answer("IGNI", false),
                                new Answer("DRY", true)
                        ))
        );
    }
}