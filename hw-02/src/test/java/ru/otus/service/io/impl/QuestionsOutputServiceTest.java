package ru.otus.service.io.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.model.Answer;
import ru.otus.model.Question;
import ru.otus.service.io.api.OutputService;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionsOutputServiceTest {

    @Mock
    OutputService<String> stringOutputService;

    @InjectMocks
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
                new Question("What development principles do not exist?",
                        of(new Answer("SOFT", false),
                                new Answer("GRASP", true),
                                new Answer("KISS", true),
                                new Answer("IGNI", false),
                                new Answer("DRY", true)
                        ))
        );
    }
}