package com.arnor4eck.ShortLinks.entity.response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public class ShortUrlsConfig {
    @Value("${url.base:lll}")
    String base;
}
