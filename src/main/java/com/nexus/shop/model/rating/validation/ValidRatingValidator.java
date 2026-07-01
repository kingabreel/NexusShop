package com.nexus.shop.model.rating.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidRatingValidator implements ConstraintValidator<ValidRating, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        
        if (value >= 0.5 && value <= 5.0) {
            return (value * 2) % 1 == 0;
        }
        
        return false;
    }
    
}
