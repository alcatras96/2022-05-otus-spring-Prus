package ru.otus.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.exception.CsvParseException;
import ru.otus.model.Answer;
import ru.otus.model.Question;
import ru.otus.service.api.CsvFileDataToQuestionsParser;

import java.util.*;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.format;

@Service
public class CsvFileDataToQuestionsParserImpl implements CsvFileDataToQuestionsParser {

    private static final String SEMICOLON_DELIMITER = ";";

    @Override
    public List<Question> parse(List<String> lines) {
        var answersByQuestions = new LinkedHashMap<String, Question>();

        String lastParsedQuestionValue = null;
        for (String line : lines) {
            var rowValues = line.split(SEMICOLON_DELIMITER);
            lastParsedQuestionValue = parseQuestionValue(lastParsedQuestionValue, rowValues[0]);

            var question = answersByQuestions.computeIfAbsent(lastParsedQuestionValue, key -> new Question(key, new ArrayList<>()));
            Answer answer = parseAnswer(rowValues);
            question.addAnswer(answer);
        }

        return answersByQuestions.values().stream().toList();
    }

    private Answer parseAnswer(String[] values) {
        if (values.length < 2) {
            throw new CsvParseException(format("Cannot parse row with values %s.", Arrays.toString(values)));
        }
        return new Answer(values[1], values.length < 3 || parseBoolean(values[2]));
    }

    private String parseQuestionValue(String lastParsedQuestion, String questionFromCsv) {
        if (lastParsedQuestion == null || !questionFromCsv.isEmpty()) {
            lastParsedQuestion = questionFromCsv;

            if (lastParsedQuestion.isEmpty()) {
                throw new CsvParseException("Cannot parse first question. Empty cell found.");
            }
        }
        return lastParsedQuestion;
    }
}
