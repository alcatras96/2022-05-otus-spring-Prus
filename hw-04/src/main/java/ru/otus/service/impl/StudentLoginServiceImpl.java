package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.model.Student;
import ru.otus.service.api.StudentLoginService;
import ru.otus.service.api.CacheService;
import ru.otus.service.io.api.StudentAuthService;

@Service
@RequiredArgsConstructor
public class StudentLoginServiceImpl implements StudentLoginService {

    private final CacheService<Student> studentCacheService;
    private final StudentAuthService studentAuthService;

    @Override
    public void login() {
        Student student = studentAuthService.authenticate();
        studentCacheService.set(student);
    }
}
