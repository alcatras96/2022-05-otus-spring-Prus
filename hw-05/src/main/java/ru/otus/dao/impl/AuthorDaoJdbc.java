package ru.otus.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import ru.otus.dao.api.AuthorDao;
import ru.otus.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.util.Collections.singletonMap;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }

    @Override
    public Long insert(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update("insert into authors (full_name) values (:full_name)",
                new MapSqlParameterSource(of("full_name", author.getFullName())), keyHolder);

        return requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<Author> getAll() {
        return namedParameterJdbcOperations.query("select id, full_name from authors", new AuthorMapper());
    }

    @Override
    public Author getById(Long id) {
        return namedParameterJdbcOperations.queryForObject("select id, full_name from authors where id = :id", singletonMap("id", id),
                new AuthorMapper());
    }

    @Override
    public void update(Author author) {
        String sql = "update authors set full_name=:full_name where id=:id";

        int rowsAffected = namedParameterJdbcOperations.update(sql, of("id", author.getId(), "full_name", author.getFullName()));

        if (rowsAffected != 1) {
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(sql, 1, rowsAffected);
        }
    }

    @Override
    public void updateFullNameById(Long id, String fullName) {
        namedParameterJdbcOperations.update("update authors set full_name=:full_name where id=:id",
                of("id", id, "full_name", fullName));
    }

    @Override
    public void deleteById(Long id) {
        namedParameterJdbcOperations.update("delete from authors where id = :id", singletonMap("id", id));
    }
}
