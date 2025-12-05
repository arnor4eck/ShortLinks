package com.arnor4eck.ShortLinks.entity;

import java.time.LocalDate;

public record ShortUrlDto(String shortUrl, String originalUrl, LocalDate createdAt, LocalDate expiredAt) {}
