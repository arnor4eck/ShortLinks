package com.arnor4eck.ShortLinks.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;

import java.time.Duration;

@TestConfiguration
public class TestConfig {
    @Bean
    public PollerSpec pollerSpec(){
        return Pollers.fixedDelay(Duration.ofMinutes(15));
    }
}
