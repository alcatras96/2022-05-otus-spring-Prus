package ru.otus.batch.model.staging;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StagingBook extends StagingEntity {

    private String name;
    private String authorId;

    public StagingBook(String originalId, String name, String authorId) {
        super(originalId);
        this.name = name;
        this.authorId = authorId;
    }
}
