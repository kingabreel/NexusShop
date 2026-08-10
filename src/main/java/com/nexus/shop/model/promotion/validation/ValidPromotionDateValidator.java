package com.nexus.shop.model.promotion.validation;

import java.time.LocalDateTime;

import com.nexus.shop.model.promotion.request.PromotionRequestDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPromotionDateValidator implements ConstraintValidator<ValidPromotionDate, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof PromotionRequestDTO dto) {
            return isValidDate(dto.startDate(), dto.endDate());
        }

        return true;
    }

    private boolean isValidDate(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null) {
            return false;
        }

        return endDate == null || startDate.isBefore(endDate);
    }
}
