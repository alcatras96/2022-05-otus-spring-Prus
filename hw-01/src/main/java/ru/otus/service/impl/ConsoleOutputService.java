package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import ru.otus.model.Question;
import ru.otus.service.api.QuestionsOutputService;

import java.io.PrintStream;
import java.util.List;

@RequiredArgsConstructor
public class ConsoleOutputService implements QuestionsOutputService {

    private final PrintStream out;

    @Override
    public void output(List<Question> questions) {
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            out.println(i + 1 + ". " + question.getValue());
            outputAnswersIfExist(question.getAnswers());
        }
    }

    private void outputAnswersIfExist(List<String> answers) {
        for (int i = 0; i < answers.size(); i++) {
            out.println("\t" + (i + 1) + ") " + answers.get(i));
        }
    }
}
