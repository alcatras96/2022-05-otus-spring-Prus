package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import ru.otus.service.api.StudentsTestingService;

@Service
@RequiredArgsConstructor
public class ApplicationListenerImpl implements ApplicationListener<ContextRefreshedEvent> {

    private final StudentsTestingService studentsTestingService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        studentsTestingService.test();
    }
}