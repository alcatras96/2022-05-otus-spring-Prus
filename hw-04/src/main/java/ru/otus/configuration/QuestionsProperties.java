package ru.otus.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "questions")
@Getter
@Setter
public class QuestionsProperties {

    private String fileName;
    private int minimumNumberOfCorrectAnswers;
}
