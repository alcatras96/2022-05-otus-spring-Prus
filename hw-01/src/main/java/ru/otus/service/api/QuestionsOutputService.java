package ru.otus.service.api;

import ru.otus.model.Question;

import java.util.List;

public interface QuestionsOutputService {

    void output(List<Question> questions);
}
