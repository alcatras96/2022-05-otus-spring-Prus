package ru.otus.service.io.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.model.Student;
import ru.otus.service.io.api.OutputService;
import ru.otus.service.io.api.UserInputService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StudentLoginServiceImplTest {

    @Mock
    private OutputService<String> outputService;

    @Mock
    private UserInputService userInputService;

    @InjectMocks
    StudentLoginServiceImpl studentLoginService;

    @Test
    @DisplayName("Should login student correctly.")
    void shouldLoginStudentCorrectly() {
        given(userInputService.readString()).willReturn("Ivan Ivanov");

        assertEquals(studentLoginService.login(), new Student("Ivan Ivanov"));
    }
}