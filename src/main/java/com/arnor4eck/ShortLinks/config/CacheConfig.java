package com.arnor4eck.ShortLinks.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.RemovalCause;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {
    public Expiry<String, Object> customExpiry(){ // кастомная политика времени хранения
        return new Expiry<>() {
            @Override
            public long expireAfterCreate(String key,
                                          Object value, long currentTime) {
                log.info("Ключ {} был добавлен в кеш", key);
                return TimeUnit.MINUTES.toNanos(15);
            }

            @Override
            public long expireAfterUpdate(String key,
                                          Object value, long currentTime, long currentDuration) {
                return currentDuration;
            }

            @Override
            public long expireAfterRead(String key,
                                        Object value, long currentTime, long currentDuration) {
                return currentDuration;
            }
        };
    }

    public Caffeine caffeine(){
        return Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfter(customExpiry())
                .evictionListener((key, value, cause) ->
                        log.info("Ключ {} был вытеснен из кеша. Причина: {}", key, cause))
                .removalListener((key, value, cause) ->
                        log.info("Ключ {} был удален из кеша. Причина: {}", key, cause));
    }

    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCaffeine(caffeine());
        manager.setCacheNames(List.of("shortUrl"));
        manager.getCacheNames().forEach(System.out::println);

        return manager;
    }
}
