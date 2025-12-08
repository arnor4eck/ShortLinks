package com.arnor4eck.ShortLinks.entity.user.role.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleConstraint {
    String message() default "Некорректный формат названия роли";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
