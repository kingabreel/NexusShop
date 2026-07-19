package com.nexus.shop.model.chatbot.request;

import com.nexus.shop.model.chatbot.enums.ChatbotOptions;
import com.nexus.shop.model.chatbot.enums.ChatbotSubOptions;

public record ChatbotRequestDto(
    ChatbotOptions option, ChatbotSubOptions subOption, String messageText, String userId) {
}
