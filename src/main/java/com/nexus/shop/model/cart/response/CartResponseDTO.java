package com.nexus.shop.model.cart.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponseDTO(
        UUID cartId,
        List<CartItemResponseDTO> items,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal total) {
}
