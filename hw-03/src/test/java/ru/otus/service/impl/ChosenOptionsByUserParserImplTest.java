package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.service.api.ChosenOptionsByUserParser;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ChosenOptionsByUserParserImplTest {

    @Autowired
    ChosenOptionsByUserParser chosenOptionsByUserParser;

    @DisplayName("Should validate answer from user correctly.")
    @ParameterizedTest
    @MethodSource("provideArgumentsForShouldParseAnswerOptionsFromUserCorrectly")
    void shouldParseAnswerOptionsFromUserCorrectly(String answerOptionsFromUser, List<Integer> expectedValues) {
        assertEquals(chosenOptionsByUserParser.parse(answerOptionsFromUser), expectedValues);
    }

    public static Stream<Arguments> provideArgumentsForShouldParseAnswerOptionsFromUserCorrectly() {
        return Stream.of(
                Arguments.of("1 2 3", List.of(0, 1, 2)),
                Arguments.of("7 3 11", List.of(6, 2, 10)),
                Arguments.of("qwerty", Collections.singletonList(-1))
        );
    }
}