package com.nexus.shop.model.cart.request;

import jakarta.validation.constraints.NotBlank;

public record ApplyCouponDTO(
        @NotBlank String code) {
}
