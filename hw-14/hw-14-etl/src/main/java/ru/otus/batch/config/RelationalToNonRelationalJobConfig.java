package ru.otus.batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import ru.otus.constants.EtlEntityMetadata;

import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static ru.otus.constants.EtlEntityMetadata.*;
import static ru.otus.constants.JobName.RELATIONAL_TO_NON_RELATIONAL_JOB;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RelationalToNonRelationalJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StagingEntityStepConfig stagingEntityStepConfig;

    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get(RELATIONAL_TO_NON_RELATIONAL_JOB.getName())
                .incrementer(new RunIdIncrementer())
                .start(stagingEntitiesRetrievalFlow())
                .next(stagingEntitiesTransformationAndSavingFlow())
                .next(stagingEntityStepConfig.cleanUpStep())
                .end()
                .build();
    }

    @Bean
    public Flow stagingEntitiesRetrievalFlow() {
        var flows = createStagingEntityRetrievalFlowsByNames(AUTHORS, COMMENTS, GENRES, BOOKS, BOOK_GENRE_RELATIONS);
        return new FlowBuilder<SimpleFlow>("stagingEntitiesRetrievalFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(flows.toArray(new Flow[0]))
                .build();
    }

    @Bean
    public Flow stagingEntitiesTransformationAndSavingFlow() {
        var flows = createStagingEntityTransformationSavingFlowsByNames(AUTHORS, GENRES, BOOKS);
        return new FlowBuilder<SimpleFlow>("stagingEntitiesTransformationAndSavingFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(flows.toArray(new Flow[0]))
                .build();
    }

    private List<Flow> createStagingEntityRetrievalFlowsByNames(EtlEntityMetadata... entitiesMetadata) {
        return stream(entitiesMetadata)
                .map(entityMetadata -> getEntityFlow(entityMetadata, "RetrievalFlow", entityMetadata.getRetrievalStep()))
                .collect(toList());
    }

    private List<Flow> createStagingEntityTransformationSavingFlowsByNames(EtlEntityMetadata... entitiesMetadata) {
        return stream(entitiesMetadata)
                .map(entityMetadata -> getEntityFlow(entityMetadata, "TransformationAndSavingFlow", entityMetadata.getTransformationAndSavingStep()))
                .collect(toList());
    }

    private Flow getEntityFlow(EtlEntityMetadata entityMetadata, String retrievalFlow, Function<StagingEntityStepConfig, Step> retrievalStep) {
        String entityName = entityMetadata.getName();
        return new FlowBuilder<SimpleFlow>(entityName + retrievalFlow)
                .start(retrievalStep.apply(stagingEntityStepConfig))
                .build();
    }
}
