package com.nexus.shop.model.cart.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponseDTO(
        UUID cartId,
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal priceAtTime,
        BigDecimal total) {
}
