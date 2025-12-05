package com.arnor4eck.ShortLinks.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.URL;

public record CreateShortUrlRequest(@URL @NotNull String originalUrl, @Positive Integer daysUrlAlive) {}
