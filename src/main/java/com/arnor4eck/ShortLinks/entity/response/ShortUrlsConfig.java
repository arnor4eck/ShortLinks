package com.arnor4eck.ShortLinks.entity.response;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ShortUrlsConfig {
    @Value("${url.base}")
    private String base;
}
