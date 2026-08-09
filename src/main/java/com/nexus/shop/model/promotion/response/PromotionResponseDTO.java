package com.nexus.shop.model.promotion.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.nexus.shop.model.product.response.ProductResponseDTO;

public record PromotionResponseDTO (
    UUID id,
    String name,
    LocalDateTime startDate,
    LocalDateTime endDate,
    Double percentage,
    List<ProductResponseDTO> products
) {
    
}
