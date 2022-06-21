package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.model.Answer;
import ru.otus.model.Question;
import ru.otus.service.api.AnswerValidator;
import ru.otus.service.api.ChosenOptionsByUserParser;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class AnswerValidatorImplTest {

    @MockBean
    ChosenOptionsByUserParser chosenOptionsByUserParser;

    @Autowired
    AnswerValidator answerValidator;

    @DisplayName("Should validate answer options from user correctly.")
    @ParameterizedTest
    @MethodSource("provideArgumentsForShouldValidateAnswerOptionsFromUserCorrectly")
    void shouldValidateAnswerOptionsFromUserCorrectly(List<Integer> answerOptions, boolean correct) {
        given(chosenOptionsByUserParser.parse(any())).willReturn(answerOptions);
        assertEquals(answerValidator.isValidAnswer(getQuestionWithAnswerOptions(), any()), correct);
    }

    @DisplayName("Should validate answer from user correctly.")
    @ParameterizedTest
    @MethodSource("provideArgumentsForShouldValidateAnswerFromUserCorrectly")
    void shouldValidateAnswerFromUserCorrectly(String answer, boolean correct) {
        assertEquals(answerValidator.isValidAnswer(getQuestion(), answer), correct);
    }

    private static Stream<Arguments> provideArgumentsForShouldValidateAnswerOptionsFromUserCorrectly() {
        return Stream.of(
                Arguments.of(List.of(1, 2, 4), true),
                Arguments.of(List.of(2, 5), false)
        );
    }

    private static Stream<Arguments> provideArgumentsForShouldValidateAnswerFromUserCorrectly() {
        return Stream.of(
                Arguments.of("object oriented programming", true),
                Arguments.of("OBJECT ORIENTED PROGRAMMING", true),
                Arguments.of("functional programming", false)
        );
    }

    private Question getQuestionWithAnswerOptions() {
        return new Question("What development principles exist?",
                of(new Answer("SOFT", false),
                        new Answer("GRASP", true),
                        new Answer("KISS", true),
                        new Answer("IGNI", false),
                        new Answer("DRY", true)
                )
        );
    }

    private Question getQuestion() {
        return new Question("How is OOP stands for?", singletonList(new Answer("Object Oriented Programming", true)));
    }
}