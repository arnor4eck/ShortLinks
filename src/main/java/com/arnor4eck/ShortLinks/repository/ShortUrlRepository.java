package com.arnor4eck.ShortLinks.repository;

import com.arnor4eck.ShortLinks.entity.short_url.ShortUrl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortUrlRepository extends CrudRepository<ShortUrl, Long> {
    Optional<ShortUrl> getByOriginalUrl(String originalUrl);
    ShortUrl getByShortCode(String shortCode);
}
