package ru.otus.repository.api;

public interface AuthorRepositoryCustom {

    void deleteByIdWithRelations(String id);
}
