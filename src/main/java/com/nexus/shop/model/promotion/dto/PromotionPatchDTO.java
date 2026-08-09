package com.nexus.shop.model.promotion.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.nexus.shop.model.promotion.validation.ValidPromotionDate;

@ValidPromotionDate
public record PromotionPatchDTO(
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Double percentage,
        List<UUID> productsId

) {

}
