package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import ru.otus.service.api.QuestionsOutputService;
import ru.otus.service.api.QuestionsReader;

@RequiredArgsConstructor
public class ApplicationListenerImpl implements ApplicationListener<ContextRefreshedEvent> {

    private final QuestionsOutputService questionsOutputService;
    private final QuestionsReader questionsReader;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        questionsOutputService.output(questionsReader.read());
    }
}