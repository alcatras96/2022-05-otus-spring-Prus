package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.exception.CsvParseException;
import ru.otus.model.Answer;
import ru.otus.model.Question;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CsvFileDataToQuestionsParserImplTest {

    @InjectMocks
    CsvFileDataToQuestionsParserImpl parser;

    @Test
    @DisplayName("Should parse CSV file data properly.")
    void shouldParseCsvFileData() {
        assertEquals(getQuestionsToCompare(), parser.parse(getCsvFileData()));
    }

    @Test
    @DisplayName("Should throw an exception for invalid CSV file data.")
    void shouldThrowExceptionIfFirstQuestionEmpty() {
        assertThrows(CsvParseException.class, () -> parser.parse(getInvalidCsvFileData()));
    }

    private List<Question> getQuestionsToCompare() {
        return of(
                new Question("What development principles do not exist?",
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
        return of("What development principles do not exist?;SOFT;false", ";GRASP;", ";KISS;", ";IGNI;false", ";DRY;",
                "How is OOP stands for?;Object Oriented Programming;");
    }

    private List<String> getInvalidCsvFileData() {
        return of(";SOFT;false", ";GRASP;", ";KISS;", ";IGNI;false", ";DRY;", "How is OOP stands for?;Object Oriented Programming;");
    }
}