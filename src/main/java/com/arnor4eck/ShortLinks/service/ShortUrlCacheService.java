package com.arnor4eck.ShortLinks.service;

import com.arnor4eck.ShortLinks.entity.short_url.ShortUrl;
import com.arnor4eck.ShortLinks.repository.ShortUrlRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ShortUrlCacheService {
    private final ShortUrlRepository shortUrlRepository;

    @Cacheable(value = "shortUrl", key = "#shortCode",
            unless = "#result == null")
    public ShortUrl findShortUrl(String shortCode){
        return shortUrlRepository.getByShortCode(shortCode);
    }
}
