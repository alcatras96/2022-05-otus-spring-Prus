package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.exception.CsvParseException;
import ru.otus.model.Answer;
import ru.otus.model.Question;
import ru.otus.service.api.CsvFileDataToQuestionsParser;
import ru.otus.service.api.I18nService;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class CsvFileDataToQuestionsParserImplTest {

    @MockBean
    I18nService i18nService;

    @Autowired
    CsvFileDataToQuestionsParser parser;

    @Test
    @DisplayName("Should parse CSV file data properly.")
    void shouldParseCsvFileData() {
        when(i18nService.getMessage(anyString())).then(returnsFirstArg());
        assertEquals(getQuestionsToCompare(), parser.parse(getCsvFileData()));
    }

    @Test
    @DisplayName("Should throw an exception for invalid CSV file data.")
    void shouldThrowExceptionIfFirstQuestionEmpty() {
        assertThrows(CsvParseException.class, () -> parser.parse(getInvalidCsvFileData()));
    }

    private List<Question> getQuestionsToCompare() {
        return of(
                new Question("What development principles exist?",
                        of(new Answer("SOFT", false),
                                new Answer("GRASP", true),
                                new Answer("KISS", true),
                                new Answer("IGNI", false),
                                new Answer("DRY", true)
                        )),
                new Question("How is OOP stands for?", singletonList(new Answer("Object Oriented Programming", true)))
        );
    }

    private List<String> getCsvFileData() {
        return of("What development principles exist?;SOFT;false", ";GRASP;", ";KISS;", ";IGNI;false", ";DRY;",
                "How is OOP stands for?;Object Oriented Programming;");
    }

    private List<String> getInvalidCsvFileData() {
        return of(";SOFT;false", ";GRASP;", ";KISS;", ";IGNI;false", ";DRY;", "How is OOP stands for?;Object Oriented Programming;");
    }
}