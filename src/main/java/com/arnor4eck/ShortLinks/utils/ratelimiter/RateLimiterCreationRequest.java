package com.arnor4eck.ShortLinks.utils.ratelimiter;

public record RateLimiterCreationRequest(String name, int limit, int refreshPeriod) {
}
