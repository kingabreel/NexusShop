package com.nexus.shop.model.rating.dto;

public record RatingUpdatePartialDTO(
        Double rating,
        String comment,
        Boolean anonymous) {
}
