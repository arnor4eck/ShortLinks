package com.arnor4eck.ShortLinks.utils.cache;

public record CacheStatisticsUnit(String name, long size,
                                  long hits, long missCount,
                                  double hitRate) {}
