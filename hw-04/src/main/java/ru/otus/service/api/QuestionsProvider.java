package ru.otus.service.api;

import ru.otus.model.Question;

import java.util.List;

public interface QuestionsProvider {

    List<Question> get();
}
