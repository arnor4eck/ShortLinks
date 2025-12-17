package com.arnor4eck.ShortLinks.config;

import com.arnor4eck.ShortLinks.utils.cache.CacheFactory;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@EnableCaching
@Configuration
@AllArgsConstructor
public class CacheConfig {

    private final CacheFactory cacheFactory;

    private final String[] cacheNames = {"shortUrl", "users"};

    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager manager = new CaffeineCacheManager();

        for(String name : cacheNames)
            manager.registerCustomCache(name,
                    (Cache<Object, Object>) cacheFactory.create(name, 500, 20));
        manager.setCacheNames(List.of(cacheNames));
        return manager;
    }
}
