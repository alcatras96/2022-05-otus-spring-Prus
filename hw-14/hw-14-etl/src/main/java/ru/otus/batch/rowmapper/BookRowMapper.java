package ru.otus.batch.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.batch.model.staging.StagingBook;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRowMapper implements RowMapper<StagingBook> {

    @Override
    public StagingBook mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new StagingBook(
                String.valueOf(rs.getLong("id")),
                rs.getString("name"),
                String.valueOf(rs.getLong("author_id"))
        );
    }
}
