package com.nexus.shop.model.product.dto;

import java.math.BigDecimal;
import com.nexus.shop.model.product.enums.Category;

public record ProductPatchDTO(
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    Category category
) {}
