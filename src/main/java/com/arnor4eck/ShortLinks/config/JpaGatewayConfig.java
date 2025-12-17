package com.arnor4eck.ShortLinks.config;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;

import java.time.LocalDateTime;
import java.util.Map;

@Configuration
@AllArgsConstructor
@Slf4j
public class JpaGatewayConfig {
    EntityManager entityManager;

    @Bean
    @Profile("dev")
    public PollerSpec pollerDev(){ // каждую минуту
        return Pollers.cron("0 * * * * *");
    }


    @Bean
    @Profile("prod")
    public PollerSpec pollerProd(){
        return Pollers.cron("0 0 2 * * *"); // каждый день в 2 ночи
    }

    @Bean
    public IntegrationFlow jpaDeactivateUrlFlow(PollerSpec poller){
        return IntegrationFlow.from(() -> new GenericMessage<>(LocalDateTime.now()),
                        e -> e.poller(poller))
                .channel("deactivateUrlsChannel")
                .handle(Jpa.updatingGateway(entityManager)
                        .nativeQuery("UPDATE short_url SET is_active = false WHERE is_active = true AND expires_at < NOW();")
                        .usePayloadAsParameterSource(false),
                        e -> e.transactional(true))
                .handle(message -> {
                    log.info("Обновление записей завершено. Ссылок деактивировано: {}", message.getPayload());
                }).get();
    }
}
