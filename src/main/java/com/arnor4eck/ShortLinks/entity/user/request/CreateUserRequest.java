package com.arnor4eck.ShortLinks.entity.user.request;

import com.arnor4eck.ShortLinks.entity.user.role.validation.RoleConstraint;
import com.arnor4eck.ShortLinks.entity.user.role.RoleDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String username,
        @NotBlank @Email(regexp = "^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$") String email,
        @NotBlank String password,
        @RoleConstraint @JsonDeserialize(using = RoleDeserializer.class) String role) {}
