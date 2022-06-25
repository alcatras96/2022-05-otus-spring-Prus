package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.configuration.QuestionsProperties;
import ru.otus.model.Student;
import ru.otus.service.api.I18nService;
import ru.otus.service.api.StudentExamResultAnnouncer;
import ru.otus.service.io.api.OutputService;

import java.util.stream.Stream;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class StudentExamResultAnnouncerImplTest {

    @MockBean
    QuestionsProperties properties;

    @MockBean
    I18nService i18nService;

    @MockBean
    OutputService<String> outputService;

    @Autowired
    StudentExamResultAnnouncer decisionManager;

    @DisplayName("Should print correct messages.")
    @ParameterizedTest
    @MethodSource("provideArgumentsForShouldOutputCorrectMessage")
    void shouldOutputCorrectMessage(String expectedMessage, int answeredQuestions) {
        when(i18nService.getMessage(anyString())).then(returnsFirstArg());
        when(properties.getMinimumNumberOfCorrectAnswers()).thenReturn(3);

        decisionManager.announce(new Student("Ivan", "Ivanov", answeredQuestions));

        verify(outputService, times(1)).output(expectedMessage);
    }

    public static Stream<Arguments> provideArgumentsForShouldOutputCorrectMessage() {
        return Stream.of(
                Arguments.of("you.passed.exam", 5),
                Arguments.of("you.did.not.pass.exam", 2)
        );
    }

}