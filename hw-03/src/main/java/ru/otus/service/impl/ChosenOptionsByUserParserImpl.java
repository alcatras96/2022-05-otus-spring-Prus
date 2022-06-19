package ru.otus.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.service.api.ChosenOptionsByUserParser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChosenOptionsByUserParserImpl implements ChosenOptionsByUserParser {

    @Override
    public List<Integer> parse(String options) {
        try {
            return Arrays.stream(options.split(" "))
                    .map(Integer::parseInt)
                    .map(integer -> integer - 1)
                    .collect(Collectors.toList());
        } catch (NumberFormatException ex) {
            return Collections.singletonList(-1);
        }
    }
}
