package ru.otus.service.io.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Student;
import ru.otus.service.io.api.StudentLoginService;
import ru.otus.service.io.api.OutputService;
import ru.otus.service.io.api.UserInputService;

@Service
@RequiredArgsConstructor
public class StudentLoginServiceImpl implements StudentLoginService {

    private final OutputService<String> outputService;
    private final UserInputService userInputService;

    @Override
    public Student login() {
        outputService.output("Please enter your first and last name.");
        String firstAndLastName = userInputService.readString();
        outputService.output(String.format("Welcome, %s!", firstAndLastName));
        return new Student(firstAndLastName);
    }
}
