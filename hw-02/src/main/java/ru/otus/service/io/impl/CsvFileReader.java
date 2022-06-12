package ru.otus.service.io.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.exception.CsvParseException;
import ru.otus.model.Answer;
import ru.otus.model.Question;
import ru.otus.service.io.api.QuestionsReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.format;

@Service
public class CsvFileReader implements QuestionsReader {

    private static final String SEMICOLON_DELIMITER = ";";

    private final String fileName;

    public CsvFileReader(@Value("${questions.file-name}") String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Question> read() {
        Map<String, List<Answer>> answersByQuestions;

        try (var inputStream = getCsvFileInputStream();
             var inputStreamReader = new InputStreamReader(inputStream);
             var csvFileReader = new BufferedReader(inputStreamReader)
        ) {
            answersByQuestions = parseCsvFile(csvFileReader);
        } catch (IOException e) {
            throw new CsvParseException(format("Cannot parse CSV file %s", fileName), e);
        }

        return answersByQuestions.entrySet()
                .stream()
                .map(entry -> new Question(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private Map<String, List<Answer>> parseCsvFile(BufferedReader csvFileReader) throws IOException {
        var answersByQuestions = new LinkedHashMap<String, List<Answer>>();
        String line;
        String lastParsedQuestion = null;
        while ((line = csvFileReader.readLine()) != null) {
            var rowValues = line.split(SEMICOLON_DELIMITER);
            lastParsedQuestion = parseQuestion(lastParsedQuestion, rowValues[0]);
            var answers = answersByQuestions.computeIfAbsent(lastParsedQuestion, key -> new ArrayList<>());
            answers.add(parseAnswer(rowValues));
        }

        return answersByQuestions;
    }

    private InputStream getCsvFileInputStream() {
        var resource = getClass().getResourceAsStream("/" + fileName);
        if (resource == null) {
            throw new CsvParseException(format("CSV file %s not found in the resources directory.", fileName));
        }

        return resource;
    }

    private Answer parseAnswer(String[] values) {
        if (values.length < 2) {
            throw new CsvParseException(format("Cannot parse row with values %s in CSV file %s.", Arrays.toString(values), fileName));
        }
        return new Answer(values[1], values.length < 3 || parseBoolean(values[2]));
    }

    private String parseQuestion(String lastParsedQuestion, String questionFromCsv) {
        if (lastParsedQuestion == null || !questionFromCsv.isEmpty()) {
            lastParsedQuestion = questionFromCsv;

            if (lastParsedQuestion.isEmpty()) {
                throw new CsvParseException("Cannot parse first question. Empty cell found.");
            }
        }
        return lastParsedQuestion;
    }
}
