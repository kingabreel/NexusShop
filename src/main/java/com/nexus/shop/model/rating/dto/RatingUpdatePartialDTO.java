package com.nexus.shop.model.rating.dto;

import com.nexus.shop.model.rating.validation.ValidRating;

public record RatingUpdatePartialDTO(
        
        @ValidRating
        Double rating,
        
        String comment,
        Boolean anonymous,
        String imageBase64) {
}
