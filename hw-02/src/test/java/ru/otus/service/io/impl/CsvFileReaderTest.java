package ru.otus.service.io.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.exception.CsvParseException;
import ru.otus.model.Answer;
import ru.otus.model.Question;
import ru.otus.service.io.api.QuestionsReader;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvFileReaderTest {

    @Test
    @DisplayName("Should parse CSV file properly.")
    void shouldParseCsvFile() {
        QuestionsReader reader = new CsvFileReader("questions.csv");
        assertEquals(getQuestionsToCompare(), reader.read());
    }

    @DisplayName("Should throw an exception for invalid files.")
    @ParameterizedTest(name = "Should throw an exception for {0} file")
    @ValueSource(strings = {"emptyFirstQuestion.csv", "missing.csv"})
    void shouldThrowExceptionIfFirstQuestionEmpty(String fileName) {
        QuestionsReader reader = new CsvFileReader(fileName);
        assertThrows(CsvParseException.class, reader::read);
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
}