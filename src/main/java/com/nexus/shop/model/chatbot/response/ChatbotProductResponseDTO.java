package com.nexus.shop.model.chatbot.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.nexus.shop.model.chatbot.enums.ChatbotResponseType;
import com.nexus.shop.model.product.enums.Category;
import com.nexus.shop.model.product.enums.Tag;

public record ChatbotProductResponseDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        Category category,
        List<Tag> tags,
        boolean highlight) implements IChatbotResponseDTO {

        @Override
        public ChatbotResponseType ResponseType() {
                return ChatbotResponseType.CHATBOT_PRODUCT_RESPONSE;
        }
}
