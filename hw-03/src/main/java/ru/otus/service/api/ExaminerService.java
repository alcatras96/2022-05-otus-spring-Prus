package ru.otus.service.api;

import ru.otus.model.Question;
import ru.otus.model.Student;

import java.util.List;

public interface ExaminerService {

    void ask(Student student, List<Question> question);

    void decide(Student student);
}
