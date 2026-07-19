package com.nexus.shop.api.chatbot.service;

import java.util.Arrays;
import java.util.List;

import com.nexus.shop.model.chatbot.entity.ChatbotMenuItem;
import com.nexus.shop.model.chatbot.enums.ChatbotOptions;

public final class ChatbotMenuFactory {

        private ChatbotMenuFactory() {
        }

        public static List<ChatbotMenuItem> build() {
                return Arrays.stream(ChatbotOptions.values())
                                .map(option -> new ChatbotMenuItem(
                                                option.name(),
                                                option.getLabel(),
                                                option.getSubOptions().stream()
                                                                .map(sub -> new ChatbotMenuItem(
                                                                                sub.name(),
                                                                                sub.getLabel(),
                                                                                List.of()))
                                                                .toList()))
                                .toList();
        }
}
