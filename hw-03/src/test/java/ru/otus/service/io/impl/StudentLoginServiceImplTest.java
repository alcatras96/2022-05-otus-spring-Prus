package ru.otus.service.io.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.model.Student;
import ru.otus.service.api.I18nService;
import ru.otus.service.io.api.OutputService;
import ru.otus.service.io.api.StudentLoginService;
import ru.otus.service.io.api.UserInputService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class StudentLoginServiceImplTest {

    @MockBean
    OutputService<String> outputService;

    @MockBean
    I18nService i18nService;

    @MockBean
    UserInputService userInputService;

    @Autowired
    StudentLoginService studentLoginService;

    @Test
    @DisplayName("Should login student correctly.")
    void shouldLoginStudentCorrectly() {
        given(userInputService.readString())
                .willReturn("Ivan")
                .willReturn("Ivanov");

        assertEquals(studentLoginService.login(), new Student("Ivan", "Ivanov"));
    }
}