package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Question;
import ru.otus.service.api.QuestionsOutputService;

import java.io.PrintStream;
import java.util.List;

import static java.util.List.of;
import static org.mockito.Mockito.*;

class ConsoleOutputServiceTest {

    @Test
    @DisplayName("Should output all questions with answers")
    void shouldOutputAllQuestionsWithAnswers() {
        PrintStream out = mock(PrintStream.class);
        QuestionsOutputService outputService = new ConsoleOutputService(out);
        outputService.output(getQuestions());
        verify(out, times(7)).println(any(String.class));
    }

    private List<Question> getQuestions() {
        return of(
                new Question("What is checked and unchecked exception?"),
                new Question("What development principles do not exist?", of("SOFT", "GRASP", "KISS", "IGNI", "DRY"))
        );
    }
}