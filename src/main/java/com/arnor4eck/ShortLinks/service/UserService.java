package com.arnor4eck.ShortLinks.service;

import com.arnor4eck.ShortLinks.entity.user.User;
import com.arnor4eck.ShortLinks.entity.user.request.CreateUserRequest;
import com.arnor4eck.ShortLinks.entity.user.role.Role;
import com.arnor4eck.ShortLinks.repository.UserRepository;
import com.arnor4eck.ShortLinks.security.AuthorizationRequest;
import com.arnor4eck.ShortLinks.security.AuthorizationResponse;
import com.arnor4eck.ShortLinks.security.cookie.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager manager;

    private final CookieUtils cookieUtils;

    @PreAuthorize("authentication.getPrincipal().getEmail() == 'arnor4eck@gmail.com'")
    public User create(CreateUserRequest request){
        return userRepository.save(User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.valueOf(request.role()))
                .build());
    }

    public AuthorizationResponse authorize(AuthorizationRequest authorizationRequest,
                                           HttpServletResponse response){
        try{
            Authentication authentication = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authorizationRequest.email(),
                            authorizationRequest.password())
            );

            String token = cookieUtils.generateToken(
                        (User) authentication.getPrincipal());

            response.addCookie(cookieUtils.generateCookie(token));

            return new AuthorizationResponse(token);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                                            e.getMessage());
        }
    }
}
