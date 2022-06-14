package ru.otus.service.io.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Answer;
import ru.otus.model.Question;
import ru.otus.service.io.api.OutputService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionsOutputService implements OutputService<Question> {

    private final OutputService<String> outputService;

    @Override
    public void output(Question question) {
        outputService.output(question.value());
        outputAnswersIfExist(question.answers());
    }

    private void outputAnswersIfExist(List<Answer> answers) {
        if (answers.size() > 1) {
            for (int i = 0; i < answers.size(); i++) {
                outputService.output("\t" + (i + 1) + ") " + answers.get(i).value());
            }
        }
    }
}
