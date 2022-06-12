package ru.otus.model;

import java.util.List;
import java.util.stream.Collectors;

public record Question(String value, List<Answer> answers) {

    public boolean withoutAnswerOptions() {
        return answers.size() <= 1;
    }

    public String getFirstAnswerValue() {
        return answers.get(0).value();
    }

    public List<Answer> getAnswersByIndices(List<Integer> indices) {
        return indices.stream().map(answers::get).collect(Collectors.toList());
    }

    public List<Answer> getCorrectAnswers() {
        return answers.stream().filter(Answer::correct).collect(Collectors.toList());
    }
}
