package ru.otus.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.model.Student;
import ru.otus.service.api.CacheService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationControllerTest {

    @MockBean
    private CacheService<Student> studentCacheService;

    @Autowired
    private Shell shell;

    @DisplayName("Should return CommandNotCurrentlyAvailable object when user doesn't login after test command evaluated")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCommandNotCurrentlyAvailableObjectWhenUserDoesNotLoginAfterTestCommandEvaluated() {
        assertThat(shell.evaluate(() -> "test")).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }
}