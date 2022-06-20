package ru.otus.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Student;
import ru.otus.service.api.ExaminerService;
import ru.otus.service.api.QuestionsProvider;
import ru.otus.service.api.StudentsTestingService;
import ru.otus.service.io.api.StudentLoginService;

@Service
@AllArgsConstructor
public class StudentsTestingServiceImpl implements StudentsTestingService {

    private final QuestionsProvider questionsProvider;
    private final ExaminerService examinerService;
    private final StudentLoginService studentLoginService;

    @Override
    public void test() {
        Student student = studentLoginService.login();

        var questions = questionsProvider.get();
        examinerService.ask(student, questions);
        examinerService.announceResults(student);
    }
}
