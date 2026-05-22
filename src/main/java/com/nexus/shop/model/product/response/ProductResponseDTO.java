package com.nexus.shop.model.product.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.nexus.shop.model.product.enums.Category;

public record ProductResponseDTO(
                UUID id,
                String name,
                String description,
                BigDecimal price,
                Integer stock,
                Category category,
                Boolean isHighlighted) {
}
