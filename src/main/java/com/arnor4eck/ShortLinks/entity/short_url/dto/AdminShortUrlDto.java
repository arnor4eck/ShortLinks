package com.arnor4eck.ShortLinks.entity.short_url.dto;

import com.arnor4eck.ShortLinks.entity.short_url.dto.views.ShortUrlAdminView;
import com.arnor4eck.ShortLinks.entity.user.UserDto;

import java.time.LocalDate;


public class AdminShortUrlDto extends ShortUrlDto implements ShortUrlAdminView {

    private final UserDto author;

    public AdminShortUrlDto(String originalUrl, String shortUrl,
                            LocalDate createdAt, LocalDate expiredAt, boolean isActive, UserDto author) {
        super(originalUrl, shortUrl, createdAt, expiredAt, isActive);
        this.author = author;
    }

    @Override
    public UserDto getAuthor() {
        return author;
    }
}
