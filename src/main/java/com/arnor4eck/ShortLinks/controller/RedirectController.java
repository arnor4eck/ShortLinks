package com.arnor4eck.ShortLinks.controller;

import com.arnor4eck.ShortLinks.entity.short_url.ShortUrl;
import com.arnor4eck.ShortLinks.service.ShortUrlsService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
@RequestMapping("/redirect_link")
public class RedirectController {

    private ShortUrlsService shortUrlsService;

    @GetMapping("/{short_code}")
    @RateLimiter(name = "redirectLimiter")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable("short_code") String shortCode){
        ShortUrl shortUrl = shortUrlsService.getRedirectUrl(shortCode);

        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .header("Location", shortUrl.getOriginalUrl())
                .build();
    }
}
