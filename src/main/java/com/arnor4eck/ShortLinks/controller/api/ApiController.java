package com.arnor4eck.ShortLinks.controller.api;

import com.arnor4eck.ShortLinks.entity.CreateShortUrlRequest;
import com.arnor4eck.ShortLinks.entity.ShortUrlDto;
import com.arnor4eck.ShortLinks.entity.response.ShortUrlsDtoFactory;
import com.arnor4eck.ShortLinks.service.ShortUrlsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
}
