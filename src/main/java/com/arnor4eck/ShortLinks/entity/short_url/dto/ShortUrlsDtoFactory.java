package com.arnor4eck.ShortLinks.entity.short_url.dto;

import com.arnor4eck.ShortLinks.entity.short_url.ShortUrl;
import com.arnor4eck.ShortLinks.entity.short_url.ShortUrlsConfig;
import com.arnor4eck.ShortLinks.entity.user.role.Role;
import com.arnor4eck.ShortLinks.entity.user.User;
import com.arnor4eck.ShortLinks.entity.user.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class ShortUrlsDtoFactory {
    private ShortUrlsConfig config;

    private ShortUrlDto create(String shortCode, String originalUrl,
                             LocalDate createdAt, LocalDate expiredAt,
                             User author){

        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            List<Role> role = (List<Role>) SecurityContextHolder.getContext()
                    .getAuthentication().getAuthorities();

            if (role.contains(Role.ADMIN)) // в зависимости от роли пользователя возвращаем определенный DTO
                return new AdminShortUrlDto(originalUrl, config.getBase() + shortCode,
                        createdAt, expiredAt,
                        author == null ? null : UserDto.fromEntity(author));
        }

        return new ShortUrlDto(originalUrl, config.getBase() + shortCode,
                createdAt, expiredAt); // если не авторизован или роль USER - обычный DTO
    }

    public ShortUrlDto createFromEntity(ShortUrl url){
        return create(url.getShortCode(), url.getOriginalUrl(),
                url.getCreatedAt(), url.getExpiresAt(),
                url.getAuthor());
    }
}
