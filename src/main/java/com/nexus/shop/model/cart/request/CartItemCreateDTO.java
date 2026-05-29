package com.nexus.shop.model.cart.request;

import java.util.UUID;

public record CartItemCreateDTO(
        UUID productId,
        Integer quantity) {
}
