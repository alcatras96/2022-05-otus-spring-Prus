package ru.otus.batch.model.staging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class StagingAuthor extends StagingEntity {

    private String fullName;

    public StagingAuthor(String originalId, String fullName) {
        super(originalId);
        this.fullName = fullName;
    }
}
