package ru.otus.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Genre;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.repository.api.BookRepository;
import ru.otus.repository.api.GenreRepository;

@Configuration
@RequiredArgsConstructor
public class RepositoryItemWriterConfig {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Bean
    public ItemWriter<Author> authorRepositoryItemWriter() {
        return getItemWriter(authorRepository);
    }

    @Bean
    public ItemWriter<Book> bookRepositoryItemWriter() {
        return getItemWriter(bookRepository);
    }

    @Bean
    public ItemWriter<Genre> genreRepositoryItemWriter() {
        return getItemWriter(genreRepository);
    }

    private <T> ItemWriter<T> getItemWriter(CrudRepository<T, String> repository) {
        var repositoryItemWriter = new RepositoryItemWriter<T>();
        repositoryItemWriter.setRepository(repository);
        return repositoryItemWriter;
    }
}
