package com.nexus.shop.model.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

import com.nexus.shop.model.product.enums.Category;

public record ProductCreateDTO(
                @NotBlank String name,
                String description,
                @NotNull @Positive BigDecimal price,
                @NotNull Integer stock,
                @NotNull Category category) {
}
