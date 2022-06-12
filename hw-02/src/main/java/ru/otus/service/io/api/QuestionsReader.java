package ru.otus.service.io.api;

import ru.otus.model.Question;

import java.util.List;

public interface QuestionsReader {

    List<Question> read();
}
