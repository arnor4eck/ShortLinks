package com.arnor4eck.ShortLinks.config;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@Component
@Profile("dev")
@EnableScheduling
@AllArgsConstructor
public class CacheStatistics {

    private final CacheManager manager;

    @Scheduled(fixedDelay = 60 * 1000)
    public void cacheStatistics(){
        manager.getCacheNames().forEach(name -> {
            Cache<String, Object> nativeCache = (Cache<String, Object>)
                    manager.getCache(name).getNativeCache();

            var currentCacheStats = nativeCache.stats();

            log.info("Кэш '{}': size = {}; hits = {}; misses = {}; hitRate = {}%",
                    name,
                    nativeCache.estimatedSize(),
                    currentCacheStats.hitCount(),
                    currentCacheStats.missCount(),
                    currentCacheStats.hitRate() * 100);
        });

    }
}
