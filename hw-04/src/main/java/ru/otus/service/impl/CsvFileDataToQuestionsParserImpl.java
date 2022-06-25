package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.exception.CsvParseException;
import ru.otus.model.Answer;
import ru.otus.model.Question;
import ru.otus.service.api.CsvFileDataToQuestionsParser;
import ru.otus.service.api.I18nService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CsvFileDataToQuestionsParserImpl implements CsvFileDataToQuestionsParser {

    private static final String SEMICOLON_DELIMITER = ";";

    private final I18nService i18nService;

    @Override
    public List<Question> parse(List<String> lines) throws CsvParseException {
        var answersByQuestions = new LinkedHashMap<String, Question>();

        String lastParsedQuestionValue = null;
        for (String line : lines) {
            var rowValues = line.split(SEMICOLON_DELIMITER);
            lastParsedQuestionValue = parseQuestionValue(lastParsedQuestionValue, rowValues[0]);

            var question = answersByQuestions.computeIfAbsent(i18nService.getMessage(lastParsedQuestionValue),
                    key -> new Question(key, new ArrayList<>()));
            Answer answer = parseAnswer(rowValues);
            question.addAnswer(answer);
        }

        return answersByQuestions.values().stream().toList();
    }

    private Answer parseAnswer(String[] values) {
        if (values.length < 2) {
            throw new CsvParseException(format("Cannot parse row with values %s.", Arrays.toString(values)));
        }
        return new Answer(i18nService.getMessage(values[1]), values.length < 3 || parseBoolean(values[2]));
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
