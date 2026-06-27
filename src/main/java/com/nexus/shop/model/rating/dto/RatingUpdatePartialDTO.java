package com.nexus.shop.model.rating.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record RatingUpdatePartialDTO(
        
        @Min(1)
        @Max(5)
        Double rating,
        
        String comment,
        Boolean anonymous,
        String imageBase64) {
}
