package com.arnor4eck.ShortLinks.utils.ratelimiter.factory;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@AllArgsConstructor
public class Resilience4jRateLimiterFactory implements RateLimiterFactory{

    RateLimiterRegistry rateLimiterRegistry;

    @Override
    public RateLimiter create(String name, int limit, int refreshPeriod) {
        return rateLimiterRegistry.rateLimiter(name,
                RateLimiterConfig.custom()
                        .limitForPeriod(limit) // запросов в единицу времени
                        .timeoutDuration(Duration.ZERO) // время в течение которого поток ждет разрешения (если лимита нет - сразу отказываем)
                        .limitRefreshPeriod(Duration.ofSeconds(refreshPeriod)) // период за который ограничивается число вызовов
                        .build());
    }
}
