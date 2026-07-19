package com.nexus.shop.model.chatbot.response;

import java.util.List;

import com.nexus.shop.model.chatbot.enums.ChatbotResponseType;

public record ChatbotProductRecommendationResponseDTO(
        List<ChatbotProductResponseDTO> products) implements IChatbotResponseDTO {

        @Override
        public ChatbotResponseType ResponseType() {
                return ChatbotResponseType.CHATBOT_PRODUCT_RECOMMENDATION;
        }
}
