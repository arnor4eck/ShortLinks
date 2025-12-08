package com.arnor4eck.ShortLinks.entity.short_url.dto.views;

import java.time.LocalDate;

public interface ShortUrlPublicView { // видят все
    String getOriginalUrl();
    String getShortUrl();
    LocalDate getCreatedAt();
    LocalDate getExpiredAt();
}
