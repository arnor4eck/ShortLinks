package com.arnor4eck.ShortLinks.controller;

import com.arnor4eck.ShortLinks.entity.user.User;
import com.arnor4eck.ShortLinks.entity.user.UserDto;
import com.arnor4eck.ShortLinks.entity.user.request.CreateUserRequest;
import com.arnor4eck.ShortLinks.repository.UserRepository;
import com.arnor4eck.ShortLinks.security.AuthorizationRequest;
import com.arnor4eck.ShortLinks.security.AuthorizationResponse;
import com.arnor4eck.ShortLinks.security.cookie.CookieUtils;
import com.arnor4eck.ShortLinks.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    private final AuthenticationManager manager;

    private final CookieUtils cookieUtils;

    @PostMapping("/create")
    public ResponseEntity<UserDto> create(@RequestBody @Valid CreateUserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(UserDto.fromEntity(userService.create(request)));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthorizationResponse> auth(@RequestBody @Valid AuthorizationRequest authorizationRequest,
                                                      HttpServletResponse response){
        try{
            User user = (User) manager.authenticate(
                    new UsernamePasswordAuthenticationToken(authorizationRequest.email(),
                    authorizationRequest.password())).getPrincipal();
            String token = cookieUtils.generateToken(user);
            Cookie cookie = new Cookie(CookieUtils.COOKIE_NAME, token);

            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge((int) cookieUtils.getMaxAge());

            response.addCookie(cookie);

            return ResponseEntity.ok(new AuthorizationResponse(token));

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
