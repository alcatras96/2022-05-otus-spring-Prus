package ru.otus.batch.model.staging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public abstract class StagingEntity implements Serializable {

    private String originalId;
    private String newId;

    public StagingEntity(String originalId) {
        this.originalId = originalId;
    }
}
