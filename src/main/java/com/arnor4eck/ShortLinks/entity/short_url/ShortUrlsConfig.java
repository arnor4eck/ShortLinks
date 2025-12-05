package com.arnor4eck.ShortLinks.entity.short_url;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ShortUrlsConfig {
    @Value("${url.base}")
    private String base;
}
