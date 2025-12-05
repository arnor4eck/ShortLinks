package com.arnor4eck.ShortLinks.service;

import com.arnor4eck.ShortLinks.entity.CreateShortUrlRequest;
import com.arnor4eck.ShortLinks.entity.ShortUrl;
import com.arnor4eck.ShortLinks.repository.ShortUrlRepository;
import com.arnor4eck.ShortLinks.utils.HashGenerator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ShortUrlsService {
    HashGenerator generator;

    ShortUrlRepository shortUrlRepository;

    public ShortUrl createUrl(CreateShortUrlRequest request){
        return shortUrlRepository.getByOriginalUrl(request.originalUrl())
                .orElseGet(() -> shortUrlRepository.save(ShortUrl.builder()
                        .createdAt(LocalDate.now())
                        .originalUrl(request.originalUrl())
                        .shortCode(generator.hash(request.originalUrl()))
                        .expiresAt(request.daysUrlAlive() == null ? null : LocalDate.now().plusDays(request.daysUrlAlive()))
                        .build()));
    }

    public ShortUrl getByShortCode(String shortCode){
        return shortUrlRepository.getByShortCode(shortCode);
    }
}
