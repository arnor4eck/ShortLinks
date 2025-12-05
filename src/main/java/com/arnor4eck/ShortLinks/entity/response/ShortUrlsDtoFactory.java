package com.arnor4eck.ShortLinks.entity.response;

import com.arnor4eck.ShortLinks.entity.ShortUrlDto;
import com.arnor4eck.ShortLinks.entity.ShortUrl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class ShortUrlsDtoFactory {
    private ShortUrlsConfig config;

    public ShortUrlDto create(String shortCode, String originalUrl, LocalDate createdAt, LocalDate expiredAt){
        return new ShortUrlDto(config.base + shortCode, originalUrl, createdAt, expiredAt);
    }

    public ShortUrlDto createFromEntity(ShortUrl url){
        return create(url.getShortCode(), url.getOriginalUrl(), url.getCreatedAt(), url.getExpiresAt());
    }
}
