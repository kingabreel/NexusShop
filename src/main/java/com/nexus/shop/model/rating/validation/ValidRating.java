package com.nexus.shop.model.rating.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidRatingValidator.class)
public @interface ValidRating {

    String message() default "Invalid evaluation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
