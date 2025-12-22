package com.arnor4eck.ShortLinks.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthorizationRequest(@NotBlank @Email(regexp = "^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$") String email,
                                   @NotBlank String password) {}
