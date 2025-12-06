package com.arnor4eck.ShortLinks.security;

import com.arnor4eck.ShortLinks.security.cookie.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Component
@AllArgsConstructor
public class CookieAccessFilter extends OncePerRequestFilter {

    private final CookieUtils cookieUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie authCookie = getCookie(request, CookieUtils.COOKIE_NAME);

        if(authCookie != null){
            String token = authCookie.getValue();
            try {
                if(cookieUtils.validateToken(token)){
                    SecurityContextHolder.getContext()
                            .setAuthentication(new PreAuthenticatedAuthenticationToken(
                                cookieUtils.getEmail(token), // если потребуется можно возвращать пользователя
                                    null,
                                    List.of(cookieUtils.getRole(token))
                            ));
                }else{
                    return;
                }
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public Cookie getCookie(HttpServletRequest request, String name){
        for(Cookie cookie : request.getCookies()){
            if(cookie.getName().equals(name))
                return cookie;
        }

        return null;
    }
}
