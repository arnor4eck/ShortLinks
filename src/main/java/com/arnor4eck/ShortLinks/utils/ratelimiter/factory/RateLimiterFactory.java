package com.arnor4eck.ShortLinks.utils.ratelimiter.factory;

import io.github.resilience4j.ratelimiter.RateLimiter;

public interface RateLimiterFactory {
    RateLimiter create(String name, int limit, int refreshPeriod);
}
