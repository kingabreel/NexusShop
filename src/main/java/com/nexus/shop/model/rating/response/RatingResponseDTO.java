package com.nexus.shop.model.rating.response;

import java.util.UUID;

public record RatingResponseDTO(
        UUID ratingId,
        Double rating,
        String comment,
        UUID productId,
        String userName,
        boolean anonymous,
        String imageUrl) {
}
