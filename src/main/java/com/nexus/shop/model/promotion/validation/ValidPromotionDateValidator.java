package com.nexus.shop.model.promotion.validation;

import com.nexus.shop.model.promotion.dto.PromotionPatchDTO;
import com.nexus.shop.model.promotion.request.PromotionRequestDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPromotionDateValidator implements ConstraintValidator<ValidPromotionDate, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof PromotionRequestDTO dto) {
            return isValidDate(dto.startDate(), dto.endDate());
        }

        if (value instanceof PromotionPatchDTO dto) {
            return isValidDate(dto.startDate(), dto.endDate());
        }

        return true;
    }

    private boolean isValidDate(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return true;
        }
        return startDate.isBefore(endDate);
    }
}
