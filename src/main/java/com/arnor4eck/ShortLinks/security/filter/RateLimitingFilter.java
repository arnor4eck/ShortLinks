package com.arnor4eck.ShortLinks.security.filter;

import com.arnor4eck.ShortLinks.security.handlers.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.RateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("customRateLimiter")
    private RateLimiter rateLimiter;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if(request.getRequestURI().startsWith("/redirect_link/")){
            if(!rateLimiter.acquirePermission()){
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(
                        objectMapper.writeValueAsString(
                                new ExceptionResponse(
                                        HttpStatus.TOO_MANY_REQUESTS.value(),
                                        "Слишком много запросов, попробуйте позже")));

                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
