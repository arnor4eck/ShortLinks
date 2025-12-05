package com.arnor4eck.ShortLinks.entity.user.request;

import com.arnor4eck.ShortLinks.utils.RoleDeserializer;
import com.arnor4eck.ShortLinks.entity.user.Role;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotNull(message = "Пропущен параметр username") @NotEmpty(message = "Имя не может быть пусты") String username,
        @NotNull(message = "Пропущен параметр email") @Email(message = "Некорректный Email", regexp = "^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$") String email,
        @NotNull(message = "Пропущен параметр password") @NotEmpty(message = "Пароль не может быть пустым") String password,
        @NotNull(message = "Пропущен параметр role") @JsonDeserialize(using = RoleDeserializer.class) Role role) {}
