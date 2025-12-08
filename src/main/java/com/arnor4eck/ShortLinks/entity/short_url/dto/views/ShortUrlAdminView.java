package com.arnor4eck.ShortLinks.entity.short_url.dto.views;

import com.arnor4eck.ShortLinks.entity.user.UserDto;

public interface ShortUrlAdminView extends ShortUrlPublicView { // видят все + админ
    UserDto getAuthor();
}
