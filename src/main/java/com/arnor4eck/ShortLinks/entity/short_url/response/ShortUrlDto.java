package com.arnor4eck.ShortLinks.entity.short_url.response;

import java.time.LocalDate;

public record ShortUrlDto(String shortUrl, String originalUrl,
                          LocalDate createdAt, LocalDate expiredAt) {}
