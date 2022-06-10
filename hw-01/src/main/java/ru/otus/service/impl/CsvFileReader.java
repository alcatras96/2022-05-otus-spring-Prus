package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import ru.otus.exception.CsvParseException;
import ru.otus.model.Question;
import ru.otus.service.api.QuestionsReader;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@RequiredArgsConstructor
public class CsvFileReader implements QuestionsReader {

    private static final String SEMICOLON_DELIMITER = ";";

    private final String fileName;

    @Override
    public List<Question> read() {
        Map<String, List<String>> answersByQuestions;


        try (InputStream inputStream = getCsvFileInputStream();
             Reader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader csvFileReader = new BufferedReader(inputStreamReader)
        ) {
            answersByQuestions = parseCsvFile(csvFileReader);
        } catch (IOException | URISyntaxException e) {
            throw new CsvParseException(String.format("Cannot parse CSV file %s", fileName), e);
        }

        return answersByQuestions.entrySet()
                .stream()
                .map(entry -> new Question(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private Map<String, List<String>> parseCsvFile(BufferedReader csvFileReader) throws IOException, URISyntaxException {
        Map<String, List<String>> answersByQuestions = new LinkedHashMap<>();
        String line;
        String lastParsedQuestion = null;
        while ((line = csvFileReader.readLine()) != null) {
            String[] rowValues = line.split(SEMICOLON_DELIMITER);
            lastParsedQuestion = parseQuestion(lastParsedQuestion, rowValues[0]);
            List<String> answers = answersByQuestions.computeIfAbsent(lastParsedQuestion, key -> new ArrayList<>());
            parseAnswer(rowValues).ifPresent(answers::add);
        }

        return answersByQuestions;
    }

    private InputStream getCsvFileInputStream() {
        InputStream resource = getClass().getResourceAsStream("/" + fileName);
        if (resource == null) {
            throw new CsvParseException(String.format("CSV file %s not found in the resources directory.", fileName));
        }

        return resource;
    }

    private Optional<String> parseAnswer(String[] values) {
        return values.length == 2 ? of(values[1]) : empty();
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
