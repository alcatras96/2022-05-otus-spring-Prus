package ru.otus.batch.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.batch.service.StagingCacheService;

import javax.cache.Cache;
import javax.cache.CacheManager;

@Service
@RequiredArgsConstructor
public class StagingCacheServiceImpl implements StagingCacheService {

    private final CacheManager cacheManager;

    @Override
    public <T> void put(String key, T entity) {
        getStagingEntitiesCache().put(key, entity);
    }

    @Override
    public <V> V get(String key) {
        return (V) getStagingEntitiesCache().get(key);
    }

    @Override
    public void clear() {
        getStagingEntitiesCache().clear();
    }

    private <V> Cache<String, V> getStagingEntitiesCache() {
        return cacheManager.getCache("staging");
    }
}
