package ru.otus.batch.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.batch.model.staging.StagingComment;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentRowMapper implements RowMapper<StagingComment> {
    @Override
    public StagingComment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new StagingComment(rs.getString("id"), rs.getString("text"), rs.getString("book_id"));
    }
}
