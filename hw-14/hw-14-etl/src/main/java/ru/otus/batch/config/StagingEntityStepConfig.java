package ru.otus.batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.batch.listener.ChunkEventsLogger;
import ru.otus.batch.model.staging.StagingEntity;
import ru.otus.batch.processor.StagingAuthorToAuthorProcessor;
import ru.otus.batch.processor.StagingBookToBookProcessor;
import ru.otus.batch.processor.StagingEntityProcessor;
import ru.otus.batch.processor.StagingGenreToGenreProcessor;
import ru.otus.batch.service.CleanUpService;
import ru.otus.batch.service.StagingCacheService;
import ru.otus.batch.writer.CacheItemWriter;
import ru.otus.constants.EtlEntityMetadata;

import static ru.otus.constants.EtlEntityMetadata.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StagingEntityStepConfig {

    private static final int CHUNK_SIZE = 3;
    private final StepBuilderFactory stepBuilderFactory;
    private final StagingItemReaderConfig stagingItemReaderConfig;
    private final RepositoryItemWriterConfig repositoryItemWriterConfig;
    private final StagingCacheService stagingCacheService;
    private final CleanUpService cleanUpService;

    @Bean
    public Step cleanUpStep() {
        return stepBuilderFactory.get("cleanUpStep")
                .tasklet(getCleanUpTasklet())
                .build();
    }

    @Bean
    public Step stagingBooksRetrievalStep() {
        return createStagingEntityRetrievalStep(BOOKS, stagingItemReaderConfig.booksReader());
    }

    @Bean
    public Step stagingAuthorsRetrievalStep() {
        return createStagingEntityRetrievalStep(AUTHORS, stagingItemReaderConfig.authorsReader());
    }

    @Bean
    public Step stagingGenresRetrievalStep() {
        return createStagingEntityRetrievalStep(GENRES, stagingItemReaderConfig.genresReader());
    }

    @Bean
    public Step stagingCommentsRetrievalStep() {
        return createStagingEntityRetrievalStep(COMMENTS, stagingItemReaderConfig.commentsReader());
    }

    @Bean
    public Step bookGenreRelationsRetrievalStep() {
        String name = BOOK_GENRE_RELATIONS.getName();
        return getStepBuilder(
                name + "RetrievalStep",
                stagingItemReaderConfig.bookGenreRelationReader(),
                new CacheItemWriter<>(stagingCacheService, name)
        ).build();
    }

    @Bean
    public Step authorsTransformationAndSavingStep() {
        return getEntityTransformationAndSavingStep(
                AUTHORS,
                stagingItemReaderConfig.stagingAuthorCacheItemReader(),
                new StagingAuthorToAuthorProcessor(),
                repositoryItemWriterConfig.authorRepositoryItemWriter());
    }

    @Bean
    public Step booksTransformationAndSavingStep() {
        return getEntityTransformationAndSavingStep(
                BOOKS,
                stagingItemReaderConfig.stagingBookCacheItemReader(),
                new StagingBookToBookProcessor(stagingCacheService),
                repositoryItemWriterConfig.bookRepositoryItemWriter());
    }

    @Bean
    public Step genresTransformationAndSavingStep() {
        return getEntityTransformationAndSavingStep(
                GENRES,
                stagingItemReaderConfig.stagingGenreCacheItemReader(),
                new StagingGenreToGenreProcessor(),
                repositoryItemWriterConfig.genreRepositoryItemWriter());
    }

    private MethodInvokingTaskletAdapter getCleanUpTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(cleanUpService);
        adapter.setTargetMethod("cleanUp");
        return adapter;
    }

    public <I, O> Step getEntityTransformationAndSavingStep(EtlEntityMetadata entityMetadata,
                                                            ItemReader<I> reader,
                                                            ItemProcessor<I, O> processor,
                                                            ItemWriter<O> writer) {
        String stepName = entityMetadata.getName() + "TransformationAndSavingStep";
        return getStepBuilder(stepName, reader, writer)
                .processor(processor)
                .build();
    }

    private <T extends StagingEntity> Step createStagingEntityRetrievalStep(EtlEntityMetadata entityMetadata, ItemReader<T> reader) {
        return getStepBuilder(entityMetadata.getName() + "RetrievalStep", reader, new CacheItemWriter<>(stagingCacheService, entityMetadata.getName()))
                .processor(new StagingEntityProcessor<>())
                .build();
    }

    private <I, O> SimpleStepBuilder<I, O> getStepBuilder(String stepName, ItemReader<I> reader, ItemWriter<O> writer) {
        return (SimpleStepBuilder<I, O>) stepBuilderFactory.get(stepName)
                .<I, O>chunk(CHUNK_SIZE)
                .reader(reader)
                .writer(writer)
                .listener(new ChunkEventsLogger(stepName));
    }
}
