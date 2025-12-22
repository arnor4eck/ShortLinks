package com.arnor4eck.ShortLinks.config.ratelimiter;

import com.arnor4eck.ShortLinks.utils.ratelimiter.RateLimiterCreationRequest;
import com.arnor4eck.ShortLinks.utils.ratelimiter.factory.Resilience4jRateLimiterFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class RateLimitersInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final Resilience4jRateLimiterFactory factory;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        RateLimiterCreationRequest[] requests = new RateLimiterCreationRequest[]{
                new RateLimiterCreationRequest("authLimiter", 10, 1),
                new RateLimiterCreationRequest("redirectLimiter", 30, 1)
        };

        for(var req : requests){
            factory.create(req.name(), req.limit(), req.refreshPeriod());
        }
    }
}
