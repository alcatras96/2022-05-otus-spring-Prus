package ru.otus.service.api;

import ru.otus.exception.CsvParseException;
import ru.otus.model.Question;

import java.util.List;

public interface CsvFileDataToQuestionsParser {

    List<Question> parse(List<String> lines) throws CsvParseException;
}
