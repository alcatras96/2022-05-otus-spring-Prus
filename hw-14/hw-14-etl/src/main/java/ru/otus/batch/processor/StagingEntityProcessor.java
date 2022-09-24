package ru.otus.batch.processor;

import org.bson.types.ObjectId;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.batch.model.staging.StagingEntity;

public class StagingEntityProcessor<I extends O, O extends StagingEntity> implements ItemProcessor<I, O> {

    @Override
    public O process(I stagingEntity) {
        stagingEntity.setNewId(new ObjectId().toString());
        return stagingEntity;
    }
}
