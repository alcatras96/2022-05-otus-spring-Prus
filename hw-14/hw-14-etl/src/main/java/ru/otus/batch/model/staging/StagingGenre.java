package ru.otus.batch.model.staging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class StagingGenre extends StagingEntity {

    private String name;

    public StagingGenre(String originalId, String name) {
        super(originalId);
        this.name = name;
    }
}
