package com.arnor4eck.ShortLinks.entity.user.role.validation;

import com.arnor4eck.ShortLinks.entity.user.role.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<RoleConstraint, String> {
    @Override
    public boolean isValid(String value,
                           ConstraintValidatorContext context) {
        try{
            Role.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
