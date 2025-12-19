package com.arnor4eck.ShortLinks.controller;

import com.arnor4eck.ShortLinks.service.CacheService;
import com.arnor4eck.ShortLinks.utils.cache.CacheStatisticsUnit;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collection;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    private final CacheService cacheService;

    private final CacheManager cacheManager;

    @GetMapping("/cache")
    public Collection<? extends CacheStatisticsUnit> cacheStatistics(){
        return cacheService.cacheStatistics(cacheManager);
    }
}
