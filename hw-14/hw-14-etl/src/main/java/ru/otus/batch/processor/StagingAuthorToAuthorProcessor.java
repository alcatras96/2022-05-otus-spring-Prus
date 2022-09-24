package ru.otus.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.batch.model.staging.StagingAuthor;
import ru.otus.model.Author;

public class StagingAuthorToAuthorProcessor implements ItemProcessor<StagingAuthor, Author> {

    @Override
    public Author process(StagingAuthor stagingAuthor) {
        return new Author(stagingAuthor.getNewId(), stagingAuthor.getFullName());
    }
}
