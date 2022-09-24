package ru.otus.batch.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.batch.model.staging.StagingGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreRowMapper implements RowMapper<StagingGenre> {

    @Override
    public StagingGenre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new StagingGenre(rs.getString("id"), rs.getString("name"));
    }
}
