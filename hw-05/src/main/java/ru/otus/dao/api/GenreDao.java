package ru.otus.dao.api;

import ru.otus.domain.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreDao extends Dao<Genre> {

    List<Genre> getByIds(Collection<Long> ids);

    List<Genre> getAllUsed();

    void updateNameById(Long id, String name);
}
