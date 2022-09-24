package ru.otus.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import ru.otus.batch.config.StagingEntityStepConfig;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum EtlEntityStepConfiguration {

    AUTHORS(StagingEntityStepConfig::stagingAuthorsRetrievalStep, StagingEntityStepConfig::authorsTransformationAndSavingStep),
    BOOKS(StagingEntityStepConfig::stagingBooksRetrievalStep, StagingEntityStepConfig::booksTransformationAndSavingStep),
    GENRES(StagingEntityStepConfig::stagingGenresRetrievalStep, StagingEntityStepConfig::genresTransformationAndSavingStep),
    COMMENTS(StagingEntityStepConfig::stagingCommentsRetrievalStep, null),
    BOOK_GENRE_RELATIONS(StagingEntityStepConfig::bookGenreRelationsRetrievalStep, null);

    private final Function<StagingEntityStepConfig, Step> retrievalStep;
    private final Function<StagingEntityStepConfig, Step> transformationAndSavingStep;
}
