package com.nexus.shop.model.rating.request;

import java.util.UUID;

public record RatingCreateDTO(
        UUID productId,
        Double rating,
        String comment,
        boolean anonymous) {

}
