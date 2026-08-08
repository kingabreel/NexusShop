package com.nexus.shop.model.store.request;

import java.util.List;

import com.nexus.shop.model.product.enums.Category;

public record StoreCreateDTO (
    String name,
    String email,
    String phone,
    List<Category> tags
) {
    public StoreCreateDTO {
        tags = tags == null ? List.of() : List.copyOf(tags);
    }
}
