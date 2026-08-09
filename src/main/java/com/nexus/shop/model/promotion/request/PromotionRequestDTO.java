package com.nexus.shop.model.promotion.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.nexus.shop.model.promotion.validation.ValidPromotionDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@ValidPromotionDate
public record PromotionRequestDTO(

        @NotBlank
        String name,

        @NotNull
        LocalDateTime startDate,
        
        @NotNull
        LocalDateTime endDate,

        @DecimalMin("0.0")
        @DecimalMax("100.0")
        Double percentage,

        List<UUID> productsId
) {

}
