package com.arnor4eck.ShortLinks.utils.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CaffeineCacheFactory implements CacheFactory{
    @Override
    public Cache<String, Object> create(String name, int size, long minutesAfterCreating){
        return Caffeine.newBuilder()
                .expireAfterWrite(minutesAfterCreating, TimeUnit.MINUTES)
                .maximumSize(size)
                .recordStats()
                .removalListener((key, value, cause) ->
                        log.info("Ключ {} был удален из кеша {}. Причина: {}", name, key, cause))
                .build();
    }
}
