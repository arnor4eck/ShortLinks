package com.arnor4eck.ShortLinks.service;

import com.arnor4eck.ShortLinks.entity.user.Role;
import com.arnor4eck.ShortLinks.entity.user.User;
import com.arnor4eck.ShortLinks.entity.user.request.CreateUserRequest;
import com.arnor4eck.ShortLinks.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    @PreAuthorize("authentication.getPrincipal() == 'arnor4eck@gmail.com'")
    public User create(CreateUserRequest request){
        return userRepository.save(User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role()).build());
    }
}
