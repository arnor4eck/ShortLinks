package com.arnor4eck.ShortLinks.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@TestConfiguration
@EnableCaching
public class TestCacheConfig {

    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCacheNames(Collections.singletonList("testCache"));
        manager.setCaffeine(Caffeine.newBuilder());

        return manager;
    }
}
