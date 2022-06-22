package ru.otus.service.api;

public interface CacheService<T> {

    void set(T object);

    T get();
}
