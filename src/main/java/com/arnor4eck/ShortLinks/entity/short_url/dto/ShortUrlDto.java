package com.arnor4eck.ShortLinks.entity.short_url.dto;

import com.arnor4eck.ShortLinks.entity.short_url.dto.views.ShortUrlPublicView;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class ShortUrlDto implements ShortUrlPublicView {

    private final String originalUrl;
    private final String shortUrl;
    private final LocalDate createdAt;
    private final LocalDate expiredAt;

    @Override
    public String getOriginalUrl() {
        return originalUrl;
    }

    @Override
    public String getShortUrl() {
        return shortUrl;
    }

    @Override
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    @Override
    public LocalDate getExpiredAt() {
        return expiredAt;
    }
}
