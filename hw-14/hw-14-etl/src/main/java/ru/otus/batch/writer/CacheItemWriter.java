package ru.otus.batch.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.util.CollectionUtils;
import ru.otus.batch.service.StagingCacheService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CacheItemWriter<T> implements ItemWriter<T> {

    private final StagingCacheService cacheService;
    private final String key;

    @Override
    public void write(List<? extends T> items) {
        List<? super T> stagingEntities = cacheService.get(key);

        if (CollectionUtils.isEmpty(stagingEntities)) {
            stagingEntities = new ArrayList<>(items);
        } else {
            stagingEntities.addAll(items);
        }
        cacheService.put(key, stagingEntities);
    }
}
