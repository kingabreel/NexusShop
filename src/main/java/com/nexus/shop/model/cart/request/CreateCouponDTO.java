package com.nexus.shop.model.cart.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nexus.shop.model.cart.enums.CouponType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCouponDTO(
        @NotBlank String code,
        @NotNull BigDecimal value,
        @NotNull CouponType type,
        @NotNull LocalDateTime expirationDate) {

}
