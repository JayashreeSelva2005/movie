package com.springbootproject.movie.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CrewMemberTypeValidator.class)
public @interface ValidateRoleTypeOfACrewMember {
    String message() default "Invalid crew member type";  // Add this line
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

