package ru.otus.service;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import ru.otus.service.impl.ApplicationListenerImpl;

@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ApplicationListenerImpl.class})
})
@SpringBootConfiguration
public class TestSpringBootConfiguration {
}
