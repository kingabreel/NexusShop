package com.nexus.shop.model.chatbot.request;

import java.util.List;

public record ChatbotOptionRequestDto(String id, String label, List<ChatbotOptionRequestDto> children) {
}
