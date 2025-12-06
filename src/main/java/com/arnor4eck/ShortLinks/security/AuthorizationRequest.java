package com.arnor4eck.ShortLinks.security;

import jakarta.validation.constraints.NotBlank;

public record AuthorizationRequest(@NotBlank(message = "Поле Email не должно быть пустым") String email,
                                   @NotBlank(message = "Поле Password не должно быть пустым") String password) {}
