package com.nexus.shop.model.rating.request;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record RatingCreateDTO(
        UUID productId,

        @Min(1)
        @Max(5)
        Double rating,
        String comment,
        boolean anonymous,
        String imageBase64) {

}
