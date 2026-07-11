package com.nexus.shop.model.store.request;

import java.util.List;

public record StoreCreateDTO (
    String name,
    String email,
    String phone,
    List<String> tags
) {

}
