package ru.otus.batch.service;

public interface StagingCacheService {

    <T> void put(String key, T entity);

    <T> T get(String key);

    void clear();
}
