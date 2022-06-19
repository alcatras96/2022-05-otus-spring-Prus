package ru.otus.service.io.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Student;
import ru.otus.service.api.I18nService;
import ru.otus.service.io.api.OutputService;
import ru.otus.service.io.api.StudentLoginService;
import ru.otus.service.io.api.UserInputService;

@Service
@RequiredArgsConstructor
public class StudentLoginServiceImpl implements StudentLoginService {

    private final OutputService<String> outputService;
    private final UserInputService userInputService;
    private final I18nService i18nService;

    @Override
    public Student login() {
        outputService.output(i18nService.getMessage("enter.your.name.and.surname"));
        String firstAndLastName = userInputService.readString();

        outputService.output(i18nService.getMessage("greeting", firstAndLastName));
        return new Student(firstAndLastName);
    }
}
