package com.nexus.shop.model.store.dto;

import java.util.UUID;

public record StoreDto (
    UUID id,
    String name,
    String email,
    String phone,
    String ownerUsername,
    String createdAt
) {
    
}
