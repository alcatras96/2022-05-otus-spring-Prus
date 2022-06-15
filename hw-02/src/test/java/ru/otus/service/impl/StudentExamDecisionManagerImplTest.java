package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.model.Student;
import ru.otus.service.api.StudentExamDecisionManager;
import ru.otus.service.io.api.OutputService;

import java.util.stream.Stream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentExamDecisionManagerImplTest {

    @Mock
    OutputService<String> outputService;

    @DisplayName("Should print correct messages.")
    @ParameterizedTest
    @MethodSource("provideArgumentsForShouldOutputCorrectMessage")
    void shouldOutputCorrectMessage(String expectedMessage, int answeredQuestions, int minimumNumberOfCorrectAnswers) {
        StudentExamDecisionManager decisionManager = new StudentExamDecisionManagerImpl(outputService, minimumNumberOfCorrectAnswers);
        decisionManager.decide(new Student("Ivan Ivanov", answeredQuestions));

        verify(outputService, times(1)).output(expectedMessage);
    }

    public static Stream<Arguments> provideArgumentsForShouldOutputCorrectMessage() {
        return Stream.of(
                // todo: internalize
                Arguments.of("You passed the exam", 5, 4),
                Arguments.of("You didn't pass the exam", 2, 6)
        );
    }

}