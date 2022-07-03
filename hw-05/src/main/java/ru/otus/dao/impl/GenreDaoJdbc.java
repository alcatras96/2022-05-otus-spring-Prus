package ru.otus.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import ru.otus.dao.api.GenreDao;
import ru.otus.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonMap;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }

    @Override
    public Long insert(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update("insert into genres (name) values (:name)",
                new MapSqlParameterSource(of("name", genre.getName())), keyHolder);

        return requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<Genre> getAll() {
        return namedParameterJdbcOperations.query("select id, name from genres", new GenreMapper());
    }

    @Override
    public Genre getById(Long id) {
        return namedParameterJdbcOperations.queryForObject("select id, name from genres where id = :id", singletonMap("id", id),
                new GenreMapper());
    }

    @Override
    public List<Genre> getByIds(Collection<Long> ids) {
        return namedParameterJdbcOperations.query("select id, name from genres where id in (:ids)", singletonMap("ids", ids),
                new GenreMapper());
    }

    @Override
    public List<Genre> getAllUsed() {
        return namedParameterJdbcOperations.query("select distinct id, name from genres g " +
                "inner join books_genres bg on bg.genre_id = g.id", new GenreMapper());
    }

    @Override
    public void update(Genre genre) {
        String sql = "update genres set name=:name where id=:id";
        int rowsAffected = namedParameterJdbcOperations.update(sql, of("id", genre.getId(), "name", genre.getName()));

        if (rowsAffected != 1) {
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(sql, 1, rowsAffected);
        }
    }

    @Override
    public void deleteById(Long id) {
        namedParameterJdbcOperations.update("delete from genres where id = :id", singletonMap("id", id));
    }

    @Override
    public void updateNameById(Long id, String name) {
        namedParameterJdbcOperations.update("update genres set name=:name where id=:id", of("id", id, "name", name));
    }
}
