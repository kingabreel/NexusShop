package com.nexus.shop.model.chatbot.request;

public record ChatbotRequestDto(
    ChatbotOptionRequestDto option, ChatbotOptionRequestDto subOption, String messageText) {
}
