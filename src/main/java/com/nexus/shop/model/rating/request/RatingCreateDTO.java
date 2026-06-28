package com.nexus.shop.model.rating.request;

import java.util.UUID;

import com.nexus.shop.model.rating.validation.ValidRating;

public record RatingCreateDTO(
        UUID productId,

        @ValidRating
        Double rating,
        String comment,
        boolean anonymous,
        String imageBase64) {

}
