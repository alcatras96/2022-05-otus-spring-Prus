package ru.otus.service.io.api;

import ru.otus.exception.CsvReadException;

import java.util.List;

public interface CsvFileReader {

    List<String> readLines() throws CsvReadException;
}
