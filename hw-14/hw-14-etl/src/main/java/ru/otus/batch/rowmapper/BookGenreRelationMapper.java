package ru.otus.batch.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.batch.model.BookGenreRelation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookGenreRelationMapper implements RowMapper<BookGenreRelation> {

    @Override
    public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BookGenreRelation(rs.getString("book_id"), rs.getString("genre_id"));
    }
}
