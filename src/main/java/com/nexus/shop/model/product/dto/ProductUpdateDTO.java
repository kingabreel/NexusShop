package com.nexus.shop.model.product.dto;

import java.math.BigDecimal;
import com.nexus.shop.model.product.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductUpdateDTO(
                @NotBlank String name,
                String description,
                @NotNull BigDecimal price,
                @NotNull Integer stock,
                @NotNull Category category) {
}
