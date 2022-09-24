package ru.otus.batch.model.staging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class StagingComment extends StagingEntity {

    private String text;
    private String bookId;

    public StagingComment(String originalId, String text, String bookId) {
        super(originalId);
        this.text = text;
        this.bookId = bookId;
    }
}
