package ru.otus.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Student;
import ru.otus.service.api.ExaminerService;
import ru.otus.service.api.QuestionsProvider;
import ru.otus.service.api.StudentsTestingService;

@Service
@AllArgsConstructor
public class StudentsTestingServiceImpl implements StudentsTestingService {

    private final QuestionsProvider questionsProvider;
    private final ExaminerService examinerService;

    @Override
    public void test(Student student) {
        var questions = questionsProvider.get();
        examinerService.ask(student, questions);
        examinerService.announceResults(student);
    }
}
