package ru.otus.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.batch.model.staging.StagingGenre;
import ru.otus.model.Genre;

public class StagingGenreToGenreProcessor implements ItemProcessor<StagingGenre, Genre> {

    @Override
    public Genre process(StagingGenre stagingGenre) {
        return new Genre(stagingGenre.getNewId(), stagingGenre.getName());
    }
}
