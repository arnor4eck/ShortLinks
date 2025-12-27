package com.arnor4eck.ShortLinks.controller;

import com.arnor4eck.ShortLinks.entity.short_url.ShortUrl;
import com.arnor4eck.ShortLinks.service.ShortUrlsService;
import com.arnor4eck.ShortLinks.service.UrlStatisticsService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/redirect_link")
public class RedirectController {

    private final UrlStatisticsService urlStatisticsService;

    @GetMapping("/{short_code}")
    @RateLimiter(name = "redirectLimiter")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable("short_code") String shortCode,
                                                      @Autowired HttpServletRequest request){
        ShortUrl shortUrl = urlStatisticsService.getUrlAndUpdateStatistics(shortCode, request);

        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .header("Location", shortUrl.getOriginalUrl())
                .build();
    }
}
