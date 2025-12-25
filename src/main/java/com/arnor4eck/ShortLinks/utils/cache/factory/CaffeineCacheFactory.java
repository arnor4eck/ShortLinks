package com.arnor4eck.ShortLinks.utils.cache.factory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CaffeineCacheFactory implements CacheFactory{
    @Override
    public Cache<Object, Object> create(String name, int size, long minutesAfterCreating){
        return Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(minutesAfterCreating, TimeUnit.MINUTES)
                .maximumSize(size)
                .removalListener((key, value, cause) ->
                        log.info("Ключ {} был удален из кеша {}. Причина: {}", name, key, cause))
                .build();
    }
}
