package com.arnor4eck.ShortLinks.service;

import com.arnor4eck.ShortLinks.entity.short_url.request.CreateShortUrlRequest;
import com.arnor4eck.ShortLinks.entity.short_url.ShortUrl;
import com.arnor4eck.ShortLinks.entity.user.User;
import com.arnor4eck.ShortLinks.entity.user.role.Role;
import com.arnor4eck.ShortLinks.repository.ShortUrlRepository;
import com.arnor4eck.ShortLinks.repository.UserRepository;
import com.arnor4eck.ShortLinks.utils.exceptions.UserNotFoundException;
import com.arnor4eck.ShortLinks.utils.ShortUrlHashGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ShortUrlsService {
    ShortUrlHashGenerator generator;

    ShortUrlRepository shortUrlRepository;

    UserRepository userRepository;

    public ShortUrl createUrl(CreateShortUrlRequest request){
        User author = request.authorId() == null ? null :
                    userRepository.findById(request.authorId()).orElseThrow(
                                () -> new UserNotFoundException("Пользователь с id %d не найден".formatted(request.authorId())));

        LocalDate createdAt = LocalDate.now();
        String hash = generator.generate(author.getEmail(), createdAt, request.originalUrl());

        return Optional.ofNullable(shortUrlRepository.getByShortCode(hash))
                .orElseGet(() -> shortUrlRepository.save(ShortUrl.builder()
                        .createdAt(createdAt)
                        .originalUrl(request.originalUrl())
                        .shortCode(hash)
                        .expiresAt(request.daysUrlAlive() == null ? null : createdAt.plusDays(request.daysUrlAlive()))
                        .author(author)
                        .build()));
    }

    @Cacheable(value = "shortUrl", key = "#shortCode",
            unless = "#result == null")
    public ShortUrl findShortUrl(String shortCode){
        return shortUrlRepository.getByShortCode(shortCode);
    }

    public ShortUrl getRedirectUrl(String shortCode){
        ShortUrl shortUrl = getByShortCode(shortCode);

        if(!shortUrl.isActive())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Срок действия истек");


        return shortUrl;
    }

    public ShortUrl getByShortCode(String shortCode){
        ShortUrl shortUrl = findShortUrl(shortCode);

        if(shortUrl == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Ссылка с кодом %s не найдена".formatted(shortCode));

        return shortUrl;
    }

    @CacheEvict(value = "shortUrl", key = "#shortCode")
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
