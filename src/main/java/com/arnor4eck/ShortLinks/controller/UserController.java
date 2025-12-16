package com.arnor4eck.ShortLinks.controller;

import com.arnor4eck.ShortLinks.entity.user.UserDto;
import com.arnor4eck.ShortLinks.entity.user.request.CreateUserRequest;
import com.arnor4eck.ShortLinks.security.AuthorizationRequest;
import com.arnor4eck.ShortLinks.security.AuthorizationResponse;
import com.arnor4eck.ShortLinks.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDto> create(@RequestBody @Valid CreateUserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(UserDto.fromEntity(userService.create(request)));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthorizationResponse> auth(@RequestBody @Valid AuthorizationRequest authorizationRequest,
                                                      HttpServletResponse response){
        return ResponseEntity.ok(
                userService.authorize(authorizationRequest, response));
    }
}
