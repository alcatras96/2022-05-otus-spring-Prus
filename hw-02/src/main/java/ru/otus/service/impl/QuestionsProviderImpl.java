package ru.otus.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Question;
import ru.otus.service.api.CsvFileDataToQuestionsParser;
import ru.otus.service.api.QuestionsProvider;
import ru.otus.service.io.api.CsvFileReader;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionsProviderImpl implements QuestionsProvider {

    private final CsvFileReader csvFileReader;
    private final CsvFileDataToQuestionsParser csvFileDataToQuestionsParser;

    @Override
    public List<Question> get() {
        var lines = csvFileReader.readLines();
        return csvFileDataToQuestionsParser.parse(lines);
    }
}
