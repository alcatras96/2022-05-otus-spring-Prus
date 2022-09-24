package ru.otus.config;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CacheConfiguration<String, Object> configuration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                        String.class,
                        Object.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB)
                                .build()
                )
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(5)))
                .build();

        CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
        cacheManager.createCache("staging", Eh107Configuration.fromEhcacheCacheConfiguration(configuration));
        return cacheManager;
    }
}
