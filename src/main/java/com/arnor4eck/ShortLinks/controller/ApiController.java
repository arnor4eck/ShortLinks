package com.arnor4eck.ShortLinks.controller;

import com.arnor4eck.ShortLinks.entity.short_url.dto.ShortUrlDto;
import com.arnor4eck.ShortLinks.entity.short_url.request.CreateShortUrlRequest;
import com.arnor4eck.ShortLinks.entity.short_url.ShortUrl;
import com.arnor4eck.ShortLinks.entity.short_url.dto.ShortUrlsDtoFactory;
import com.arnor4eck.ShortLinks.entity.user.User;
import com.arnor4eck.ShortLinks.service.ShortUrlsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/short_links", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiController {
    ShortUrlsService shortUrlsService;

    ShortUrlsDtoFactory shortUrlsDtoFactory;

    @PostMapping("/create")
    public ResponseEntity<ShortUrlDto> createUrl(@Valid @RequestBody CreateShortUrlRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(shortUrlsDtoFactory.createFromEntity(
                                shortUrlsService.createUrl(request)));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<ShortUrlDto> getUrlByShortCode(@PathVariable("shortCode") String shortCode){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(shortUrlsDtoFactory.createFromEntity(shortUrlsService.getByShortCode(shortCode)));
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteUrlByShortCode(@PathVariable("shortCode") String shortCode,
                                                     @AuthenticationPrincipal User authUser){
        return ResponseEntity.status(
                shortUrlsService.deleteByShortCode(shortCode, authUser) ?
                        HttpStatus.ACCEPTED :
                        HttpStatus.FORBIDDEN).build();
    }
}
