package ru.otus.batch.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.batch.service.CleanUpService;
import ru.otus.batch.service.StagingCacheService;

@Service
@RequiredArgsConstructor
public class CleanUpServiceImpl implements CleanUpService {

    private final StagingCacheService cacheService;

    @Override
    public void cleanUp() {
        cacheService.clear();
    }
}
