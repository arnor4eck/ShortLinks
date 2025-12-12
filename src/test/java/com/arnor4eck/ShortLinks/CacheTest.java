package com.arnor4eck.ShortLinks;

import com.arnor4eck.ShortLinks.config.TestCacheConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

@AutoConfigureCache(cacheProvider = CacheType.CAFFEINE)
@ExtendWith(SpringExtension.class)
@Import(TestCacheConfig.class)
class CacheTest {
    @Autowired
    private CacheManager cacheManager;

    @Test
    @Description("Содержит только один параметр - testCache")
    public void testCache(){
        Collection<String> cache = cacheManager.getCacheNames();

        Assertions.assertEquals(1, cache.size());
        Assertions.assertTrue(cache.contains("testCache"));
    }

    @Test
    public void testCacheShouldReturnNull(){
        Cache cache = cacheManager.getCache("test");

        Assertions.assertNull(cache);
    }
}
