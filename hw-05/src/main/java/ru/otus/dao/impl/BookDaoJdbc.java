package ru.otus.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import ru.otus.dao.api.BookDao;
import ru.otus.dao.api.GenreDao;
import ru.otus.dao.impl.relation.BookGenreRelation;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;

import static java.util.Collections.singletonMap;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Service
@AllArgsConstructor
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final GenreDao genreDao;

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            long authorId = rs.getLong("author_id");
            return new Book(id, name, authorId);
        }

    }

    private static class BooksWilAllDataResultSetExtractor implements ResultSetExtractor<Map<Long, Book>> {

        @Override
        public Map<Long, Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Book> booksMap = new HashMap<>();
            while (rs.next()) {
                long id = rs.getLong("id");
                Book book = booksMap.get(id);
                if (book == null) {
                    book = new Book(id, rs.getString("name"), rs.getLong("author_id"));
                    booksMap.put(book.getId(), book);
                }
                book.getGenres()
                        .add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
            }
            return booksMap;
        }
    }

    private static class BooksResultSetExtractor implements ResultSetExtractor<Map<Long, Book>> {

        @Override
        public Map<Long, Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Book> books = new HashMap<>();
            while (rs.next()) {
                long id = rs.getLong("id");
                Book book = new Book(id, rs.getString("name"), rs.getLong("author_id"));
                books.put(id, book);
            }
            return books;
        }
    }

    private static class BookGenreMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("book_id");
            long genreId = rs.getLong("genre_id");
            return new BookGenreRelation(bookId, genreId);
        }
    }

    @Override
    public Long insert(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update("insert into books (name, author_id) values (:name, :author_id)",
                new MapSqlParameterSource(of("name", book.getName(), "author_id", book.getAuthorId())), keyHolder, new String[]{"id"});

        long bookId = requireNonNull(keyHolder.getKey()).longValue();

        saveRelationsWithGenres(book.getGenres(), bookId);

        return bookId;
    }

    @Override
    public List<Book> getAll() {
        Map<Long, Book> books = namedParameterJdbcOperations.query("select id, name, author_id from books", new BooksResultSetExtractor());
        List<BookGenreRelation> relations = namedParameterJdbcOperations.query("select book_id, genre_id from books_genres",
                new BookGenreMapper());
        List<Genre> genres = genreDao.getAllUsed();

        mergeBooksWithGenres(books, genres, relations);

        return convertToList(books);
    }

    @Override
    public List<Book> getAllInOneQuery() {
        Map<Long, Book> books = namedParameterJdbcOperations.query("select b.id, b.name, b.author_id, " +
                        "g.id as genre_id, g.name as genre_name from books b " +
                        "left join books_genres bg on bg.book_id = b.id left join genres g on g.id = bg.genre_id",
                new BooksWilAllDataResultSetExtractor());

        return convertToList(books);
    }

    @Override
    public Book getById(Long id) {
        Book book = namedParameterJdbcOperations.queryForObject("select id, name, author_id from books where id = :id",
                singletonMap("id", id), new BookMapper());
        List<BookGenreRelation> relations = namedParameterJdbcOperations.query("select book_id, genre_id from books_genres " +
                "where book_id=:book_id", of("book_id", id), new BookGenreMapper());
        List<Genre> genres = getGenresByIds(relations);

        requireNonNull(book).setGenres(genres);

        return book;
    }

    @Override
    public void update(Book book) {
        Long bookId = book.getId();
        String sql = "update books set name=:name, author_id=:author_id where id=:id";
        int rowsAffected = namedParameterJdbcOperations.update(sql,
                of("id", bookId, "name", book.getName(), "author_id", book.getAuthorId()));

        if (rowsAffected != 1) {
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(sql, 1, rowsAffected);
        }

        deleteOldRelationsWithGenres(bookId);
        saveRelationsWithGenres(book.getGenres(), bookId);
    }

    @Override
    public void updateName(Long id, String name) {
        namedParameterJdbcOperations.update("update books set name=:name where id=:id", of("id", id, "name", name));
    }

    @Override
    public void deleteById(Long id) {
        namedParameterJdbcOperations.update("delete from books where id = :id", singletonMap("id", id));
    }

    private void saveRelationsWithGenres(List<Genre> genres, long bookId) {
        Map<String, ? extends Number>[] batchValues = genres
                .stream()
                .map(Genre::getId)
                .map(genreId -> of("book_id", bookId, "genre_id", genreId))
                .toArray((IntFunction<Map<String, ? extends Number>[]>) Map[]::new);

        namedParameterJdbcOperations.batchUpdate("insert into books_genres (book_id,genre_id) values (:book_id, :genre_id)", batchValues);
    }

    private List<Book> convertToList(Map<Long, Book> books) {
        return requireNonNull(books).values().stream().toList();
    }

    private void deleteOldRelationsWithGenres(long bookId) {
        namedParameterJdbcOperations.update("delete from books_genres where book_id = :book_id", singletonMap("book_id", bookId));
    }

    private List<Genre> getGenresByIds(List<BookGenreRelation> relations) {
        Set<Long> genresIds = relations
                .stream()
                .map(BookGenreRelation::getGenreId)
                .collect(toSet());

        return genreDao.getByIds(genresIds);
    }

    private void mergeBooksWithGenres(Map<Long, Book> books, List<Genre> genres, List<BookGenreRelation> relations) {
        Map<Long, Genre> genresMap = genres
                .stream()
                .collect(toMap(Genre::getId, Function.identity()));

        relations.forEach(relation ->
                requireNonNull(books)
                        .get(relation.getBookId())
                        .getGenres()
                        .add(genresMap.get(relation.getGenreId()))
        );
    }
}
