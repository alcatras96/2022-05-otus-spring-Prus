package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.service.api.I18nService;

@Service
@RequiredArgsConstructor
public class I18ServiceImpl implements I18nService {

    private final MessageSource messageSource;

    @Override
    public String getMessage(String key, String... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}
