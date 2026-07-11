package com.nexus.shop.model.chatbot.entity;

import java.util.List;

public record ChatbotMenuItem(
        String id,
        String label,
        List<ChatbotMenuItem> children
) {}