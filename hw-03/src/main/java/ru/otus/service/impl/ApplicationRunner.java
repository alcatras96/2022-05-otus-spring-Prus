package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.otus.service.api.StudentsTestingService;

@Service
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {

    private final StudentsTestingService studentsTestingService;

    @Override
    public void run(String... args) {
        studentsTestingService.test();
    }
}