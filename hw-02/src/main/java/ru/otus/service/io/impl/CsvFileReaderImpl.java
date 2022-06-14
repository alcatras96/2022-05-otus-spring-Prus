package ru.otus.service.io.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.exception.CsvReadException;
import ru.otus.service.io.api.CsvFileReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class CsvFileReaderImpl implements CsvFileReader {

    private final String fileName;

    public CsvFileReaderImpl(@Value("${questions.file-name}") String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<String> readLines() {
        try (var inputStream = getCsvFileInputStream();
             var inputStreamReader = new InputStreamReader(inputStream);
             var csvFileReader = new BufferedReader(inputStreamReader)
        ) {
            return csvFileReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new CsvReadException(format("Cannot read CSV file %s", fileName), e);
        }
    }

    private InputStream getCsvFileInputStream() {
        var resource = getClass().getResourceAsStream("/" + fileName);
        if (resource == null) {
            throw new CsvReadException(format("CSV file %s not found in the resources directory.", fileName));
        }

        return resource;
    }
}
