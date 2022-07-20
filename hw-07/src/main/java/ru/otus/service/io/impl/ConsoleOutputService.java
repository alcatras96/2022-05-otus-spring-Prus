package ru.otus.service.io.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.service.io.api.OutputService;

import java.io.PrintStream;

@Service
public class ConsoleOutputService implements OutputService<String> {

    private final PrintStream out;

    public ConsoleOutputService(@Value("#{T(System).out}") PrintStream out) {
        this.out = out;
    }

    @Override
    public void output(String string) {
        out.println(string);
    }
}
