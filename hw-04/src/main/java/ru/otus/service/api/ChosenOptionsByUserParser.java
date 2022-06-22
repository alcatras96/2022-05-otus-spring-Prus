package ru.otus.service.api;

import java.util.List;

public interface ChosenOptionsByUserParser {

    List<Integer> parse(String options);
}
