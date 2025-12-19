package com.arnor4eck.ShortLinks.utils.cache;

import com.arnor4eck.ShortLinks.service.CacheService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final CacheManager cacheManager;

    private final CacheService cacheService;

    @Scheduled(fixedDelay = 60 * 1000)
    public void cacheStatistics(){
        cacheService.cacheStatistics(cacheManager).forEach(cacheStatisticsUnit -> {
            log.info("Кэш '{}': size = {}; hits = {}; misses = {}; hitRate = {}%",
                    cacheStatisticsUnit.name(), cacheStatisticsUnit.size(),
                    cacheStatisticsUnit.hits(), cacheStatisticsUnit.missCount(),
                    cacheStatisticsUnit.hitRate() * 100);
        });
    }
}
