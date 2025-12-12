package com.arnor4eck.ShortLinks.service;

import com.arnor4eck.ShortLinks.entity.short_url.request.CreateShortUrlRequest;
import com.arnor4eck.ShortLinks.entity.short_url.ShortUrl;
import com.arnor4eck.ShortLinks.entity.user.User;
import com.arnor4eck.ShortLinks.entity.user.role.Role;
import com.arnor4eck.ShortLinks.repository.ShortUrlRepository;
import com.arnor4eck.ShortLinks.repository.UserRepository;
import com.arnor4eck.ShortLinks.utils.HashGenerator;
import com.arnor4eck.ShortLinks.utils.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ShortUrlsService {
    HashGenerator generator;

    ShortUrlRepository shortUrlRepository;

    UserRepository userRepository;

    public ShortUrl createUrl(CreateShortUrlRequest request){
        User author = request.authorId() == null ? null :
                    userRepository.findById(request.authorId()).orElseThrow(
                                () -> new UserNotFoundException("Пользователь с id %d не найден".formatted(request.authorId())));

        return shortUrlRepository.getByOriginalUrl(request.originalUrl())
                .orElseGet(() -> shortUrlRepository.save(ShortUrl.builder()
                        .createdAt(LocalDate.now())
                        .originalUrl(request.originalUrl())
                        .shortCode(generator.hash(request.originalUrl()))
                        .expiresAt(request.daysUrlAlive() == null ? null : LocalDate.now().plusDays(request.daysUrlAlive()))
                        .author(author)
                        .build()));
    }

    @Cacheable(value = "shortUrl", key = "#shortCode")
    public ShortUrl getByShortCode(String shortCode){
        return shortUrlRepository.getByShortCode(shortCode);
    }

    @CacheEvict(value = "shortUrl", key = "#shortCode")
    @Transactional
    public boolean deleteByShortCode(String shortCode,
                                    Authentication authentication){
        ShortUrl shortUrl = shortUrlRepository.getByShortCode(shortCode);

        if(shortUrl == null)
            return true;

        if(shortUrl.getAuthor().getEmail().equals((String) authentication.getPrincipal()) // если тот же автор
            || authentication.getAuthorities().contains(Role.ADMIN)) {  // или если админ

            this.shortUrlRepository.deleteByShortCode(shortCode);
            return true;
        }

        return false;
    }
}
