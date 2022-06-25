package ru.otus.service.io.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.service.io.api.OutputService;
import ru.otus.service.io.api.UserInputService;

import java.io.InputStream;
import java.util.Scanner;

@Service
public class UserInputServiceImpl implements UserInputService {

    private final OutputService<String> outputService;
    private final Scanner input;

    public UserInputServiceImpl(OutputService<String> outputService, @Value("#{T(System).in}") InputStream input) {
        this.outputService = outputService;
        this.input = new Scanner(input);
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        outputService.output(prompt);
        return input.nextLine();
    }

    @Override
    public String readString() {
        return input.nextLine();
    }
}
