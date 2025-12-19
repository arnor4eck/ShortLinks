package com.arnor4eck.ShortLinks.service;

import com.arnor4eck.ShortLinks.utils.cache.CacheStatisticsUnit;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CacheService {

    public Collection<? extends CacheStatisticsUnit> cacheStatistics(CacheManager manager){
        List<CacheStatisticsUnit> statistics = new ArrayList<>(manager.getCacheNames().size());

        manager.getCacheNames().forEach(name -> {
            Cache<String, Object> nativeCache = (Cache<String, Object>)
                    manager.getCache(name).getNativeCache();

            CacheStats currentCacheStats = nativeCache.stats();

            statistics.add(new CacheStatisticsUnit(name,
                    nativeCache.estimatedSize(),currentCacheStats.hitCount(),
                    currentCacheStats.missCount(), currentCacheStats.hitRate()));
        });

        return statistics;
    }
}
