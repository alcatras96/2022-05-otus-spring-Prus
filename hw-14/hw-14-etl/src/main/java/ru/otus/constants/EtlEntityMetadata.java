package ru.otus.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import ru.otus.batch.config.StagingEntityStepConfig;

import java.util.function.Function;

@RequiredArgsConstructor
public enum EtlEntityMetadata {

    AUTHORS("authors", EtlEntityStepConfiguration.AUTHORS),
    BOOKS("books", EtlEntityStepConfiguration.BOOKS),
    GENRES("genres", EtlEntityStepConfiguration.GENRES),
    COMMENTS("comments", EtlEntityStepConfiguration.COMMENTS),
    BOOK_GENRE_RELATIONS("bookGenreRelations", EtlEntityStepConfiguration.BOOK_GENRE_RELATIONS);

    @Getter
    private final String name;
    private final EtlEntityStepConfiguration flowConfiguration;

    public Function<StagingEntityStepConfig, Step> getRetrievalStep() {
        return flowConfiguration.getRetrievalStep();
    }

    public Function<StagingEntityStepConfig, Step> getTransformationAndSavingStep() {
        return flowConfiguration.getTransformationAndSavingStep();
    }
}
