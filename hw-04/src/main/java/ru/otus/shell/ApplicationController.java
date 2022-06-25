package ru.otus.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.model.Student;
import ru.otus.service.api.CacheService;
import ru.otus.service.api.I18nService;
import ru.otus.service.api.StudentLoginService;
import ru.otus.service.api.StudentsTestingService;

import static org.springframework.shell.Availability.available;
import static org.springframework.shell.Availability.unavailable;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationController {

    private final CacheService<Student> studentCacheService;
    private final StudentsTestingService studentsTestingService;
    private final StudentLoginService studentLoginService;
    private final I18nService i18nService;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public void login() {
        studentLoginService.login();
    }

    @ShellMethod(value = "Test command", key = {"t", "test"})
    @ShellMethodAvailability(value = "isTestCommandAvailable")
    public void test() {
        studentsTestingService.test(studentCacheService.get());
    }

    private Availability isTestCommandAvailable() {
        return studentCacheService.get() == null ? unavailable(i18nService.getMessage("login.first")) : available();
    }
}
