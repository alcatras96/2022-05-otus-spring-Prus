package ru.otus.service.io.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.configuration.QuestionsProperties;
import ru.otus.exception.CsvReadException;
import ru.otus.service.io.api.CsvFileReader;

import java.util.List;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class CsvFileReaderImplTest {

    @MockBean
    QuestionsProperties properties;

    @Autowired
    CsvFileReader reader;

    @Test
    @DisplayName("Should parse CSV file properly.")
    void shouldParseCsvFile() {
        when(properties.getFileName()).thenReturn("questions.csv");
        assertEquals(getLinesToCompare(), reader.readLines());
    }

    @Test
    @DisplayName("Should throw an exception if file not found.")
    void shouldThrowExceptionIfFileNotFound() {
        when(properties.getFileName()).thenReturn("missing.csv");
        assertThrows(CsvReadException.class, reader::readLines);
    }

    private List<String> getLinesToCompare() {
        return of("What development principles exist?;SOFT;false", ";GRASP;", ";KISS;", ";IGNI;false", ";DRY;",
                "How is OOP stands for?;Object Oriented Programming;");
    }
}