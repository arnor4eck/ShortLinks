package com.arnor4eck.ShortLinks.utils.cache;

public interface CacheFactory{
    Object create(String name, int size, long minutesAfterCreating);
}
