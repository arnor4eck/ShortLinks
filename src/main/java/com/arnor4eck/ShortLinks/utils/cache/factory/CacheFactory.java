package com.arnor4eck.ShortLinks.utils.cache.factory;

import com.github.benmanes.caffeine.cache.Cache;

public interface CacheFactory{
    Cache<Object, Object> create(String name, int size, long minutesAfterCreating);
}
