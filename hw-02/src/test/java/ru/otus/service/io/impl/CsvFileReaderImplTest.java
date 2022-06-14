package ru.otus.service.io.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.exception.CsvReadException;
import ru.otus.service.io.api.CsvFileReader;

import java.util.List;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvFileReaderImplTest {

    @Test
    @DisplayName("Should parse CSV file properly.")
    void shouldParseCsvFile() {
        CsvFileReader reader = new CsvFileReaderImpl("questions.csv");
        assertEquals(getLinesToCompare(), reader.readLines());
    }

    @Test
    @DisplayName("Should throw an exception if file not found.")
    void shouldThrowExceptionIfFileNotFound() {
        CsvFileReader reader = new CsvFileReaderImpl("missing.csv");
        assertThrows(CsvReadException.class, reader::readLines);
    }

    private List<String> getLinesToCompare() {
        return of("What development principles do not exist?;SOFT;false", ";GRASP;", ";KISS;", ";IGNI;false", ";DRY;",
                "How is OOP stands for?;Object Oriented Programming;");
    }
}