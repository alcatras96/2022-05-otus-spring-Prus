package ru.otus.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.model.Student;
import ru.otus.service.api.CacheService;

@Service
public class StudentCacheService implements CacheService<Student> {

    private Student cachedStudent;

    @Override
    public void set(Student student) {
        cachedStudent = student;
    }

    @Override
    public Student get() {
        return cachedStudent;
    }
}
