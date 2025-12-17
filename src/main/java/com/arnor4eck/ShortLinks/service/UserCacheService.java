package com.arnor4eck.ShortLinks.service;

import com.arnor4eck.ShortLinks.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserCacheService {

    private final UserDetailsService userDetailsService;

    @Cacheable(cacheNames = "users", key = "#email",
            unless = "#result == null")
    public User findUserByEmail(String email){
        log.info("bdb");
        return (User) userDetailsService.loadUserByUsername(email);
    }

}
