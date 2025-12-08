package com.arnor4eck.ShortLinks.entity.short_url.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.URL;

public record CreateShortUrlRequest(@URL @NotBlank String originalUrl,
                                    @Positive @Nullable Integer daysUrlAlive,
                                    @Positive @Nullable Long authorId) {}
