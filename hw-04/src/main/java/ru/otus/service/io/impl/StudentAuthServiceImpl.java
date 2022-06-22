package ru.otus.service.io.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Student;
import ru.otus.service.api.I18nService;
import ru.otus.service.io.api.OutputService;
import ru.otus.service.io.api.StudentAuthService;
import ru.otus.service.io.api.UserInputService;

@Service
@RequiredArgsConstructor
public class StudentAuthServiceImpl implements StudentAuthService {

    private final OutputService<String> outputService;
    private final UserInputService userInputService;
    private final I18nService i18nService;

    @Override
    public Student authenticate() {
        var name = askStudentForInformation("enter.your.name");
        var surname = askStudentForInformation("enter.your.surname");
        var student = new Student(name, surname);

        outputService.output(i18nService.getMessage("greeting", student.getFirstAndLastName()));

        return student;
    }

    private String askStudentForInformation(String messageToAsk) {
        outputService.output(i18nService.getMessage(messageToAsk));
        return userInputService.readString();
    }
}
