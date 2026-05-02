package com.nexus.shop.model.product.response;

import java.math.BigDecimal;
import com.nexus.shop.model.product.enums.Category;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        Category category
) {}