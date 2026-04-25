package com.nexus.shop.model.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

import com.nexus.shop.model.product.enums.Category;

// A partir do java 17, foi lançado o record
// Basicamente você só define as variaveis como parametros e ele já gera o construtor, getters e setters automaticamente :)
public record ProductCreateDTO(
        @NotBlank String name,
        String description,
        @NotNull @Positive BigDecimal price,
        @NotNull Integer stock,
        @NotNull Category category
) {}