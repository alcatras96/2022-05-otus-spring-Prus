package ru.otus.batch.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.batch.model.staging.StagingAuthor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorRowMapper implements RowMapper<StagingAuthor> {

    @Override
    public StagingAuthor mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new StagingAuthor(rs.getString("id"), rs.getString("full_name"));
    }

}
