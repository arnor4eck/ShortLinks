package com.arnor4eck.ShortLinks.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.Duration;

@Slf4j
@Configuration
public class RateLimitConfig {

    @Bean
    public RateLimiterConfig rateLimiterConfig(){
        return RateLimiterConfig.custom()
                .limitForPeriod(30) // 30 запросов в единицу времени
                .timeoutDuration(Duration.ZERO) // время в течение которого поток ждет разрешения (если лимита нет - сразу отказываем)
                .limitRefreshPeriod(Duration.ofSeconds(1)) // период за который ограничивается число вызовов
                .build();
    }

    @Bean
    public RateLimiter customRateLimiter(RateLimiterConfig config){
        RateLimiter limiter =  RateLimiter.of("customRateLimiter", rateLimiterConfig());
        limiter.getEventPublisher()
                .onFailure(rate -> {
                    log.warn(rate.getRateLimiterName() + ": " + rate.getEventType());
                });

        return limiter;
    }
}
