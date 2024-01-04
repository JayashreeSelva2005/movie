package com.springbootproject.movie.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class CrewMemberTypeValidator implements ConstraintValidator<ValidateRoleTypeOfACrewMember,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        List<String> crewMemberRoleTypes = Arrays.asList("Actress", "Actor", "Director", "Producer", "Music Director", "Musicians", "Singer");
        return crewMemberRoleTypes.contains(value);
    }

}
