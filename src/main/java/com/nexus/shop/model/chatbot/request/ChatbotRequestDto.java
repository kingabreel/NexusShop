package com.nexus.shop.model.chatbot.request;

public record ChatbotRequestDto(
    String messageId, String messageKey, String messageText, String userId) {
}
