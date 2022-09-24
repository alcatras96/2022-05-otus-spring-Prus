package ru.otus.batch.reader;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import ru.otus.batch.service.StagingCacheService;

import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class StagingItemCacheReader<T> implements ItemReader<T> {

    private final StagingCacheService cacheService;
    private final String key;
    private List<T> items;

    @Override
    public T read() {
        if (items == null) {
            items = (List<T>) ofNullable(cacheService.get(key)).orElse(Collections.emptyList());
        }

        return items.isEmpty() ? null : items.remove(0);
    }
}
