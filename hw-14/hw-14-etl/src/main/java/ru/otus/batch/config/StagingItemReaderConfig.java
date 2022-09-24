package ru.otus.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import ru.otus.batch.model.BookGenreRelation;
import ru.otus.batch.model.staging.StagingAuthor;
import ru.otus.batch.model.staging.StagingBook;
import ru.otus.batch.model.staging.StagingComment;
import ru.otus.batch.model.staging.StagingGenre;
import ru.otus.batch.reader.StagingItemCacheReader;
import ru.otus.batch.rowmapper.*;
import ru.otus.batch.service.StagingCacheService;

import javax.sql.DataSource;
import java.util.Map;

import static ru.otus.constants.EtlEntityMetadata.*;

@RequiredArgsConstructor
@Configuration
public class StagingItemReaderConfig {

    private final DataSource dataSource;
    private final StagingCacheService stagingCacheService;

    @StepScope
    @Bean
    public ItemReader<StagingBook> booksReader() {
        return getItemReader(new BookRowMapper(), getQueryProvider("books", "id"));
    }

    @StepScope
    @Bean
    public ItemReader<StagingAuthor> authorsReader() {
        return getItemReader(new AuthorRowMapper(), getQueryProvider("authors", "id"));
    }

    @StepScope
    @Bean
    public ItemReader<StagingGenre> genresReader() {
        return getItemReader(new GenreRowMapper(), getQueryProvider("genres", "id"));
    }

    @StepScope
    @Bean
    public ItemReader<StagingComment> commentsReader() {
        return getItemReader(new CommentRowMapper(), getQueryProvider("comments", "id"));
    }

    @StepScope
    @Bean
    public ItemReader<BookGenreRelation> bookGenreRelationReader() {
        return getItemReader(new BookGenreRelationMapper(), getQueryProvider("books_genres", "book_id"));
    }

    @StepScope
    @Bean
    public ItemReader<StagingBook> stagingBookCacheItemReader() {
        return new StagingItemCacheReader<>(stagingCacheService, BOOKS.getName());
    }

    @StepScope
    @Bean
    public ItemReader<StagingAuthor> stagingAuthorCacheItemReader() {
        return new StagingItemCacheReader<>(stagingCacheService, AUTHORS.getName());
    }

    @StepScope
    @Bean
    public ItemReader<StagingGenre> stagingGenreCacheItemReader() {
        return new StagingItemCacheReader<>(stagingCacheService, GENRES.getName());
    }

    private <T> ItemReader<T> getItemReader(RowMapper<T> rowMapper, PagingQueryProvider queryProvider) {
        var itemReader = new JdbcPagingItemReader<T>();
        itemReader.setDataSource(dataSource);
        itemReader.setFetchSize(100);
        itemReader.setPageSize(100);
        itemReader.setRowMapper(rowMapper);
        itemReader.setQueryProvider(queryProvider);
        return itemReader;
    }

    private PagingQueryProvider getQueryProvider(String tableName, String sortingColumn) {
        var sortKeys = Map.of(sortingColumn, Order.ASCENDING);
        var queryProvider = new PostgresPagingQueryProvider();
        queryProvider.setSelectClause("*");
        queryProvider.setFromClause(tableName);
        queryProvider.setSortKeys(sortKeys);
        return queryProvider;
    }
}
