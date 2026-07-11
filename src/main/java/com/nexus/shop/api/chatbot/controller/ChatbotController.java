package com.nexus.shop.api.chatbot.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.shop.api.chatbot.service.ChatbotMenuFactory;
import com.nexus.shop.api.chatbot.service.ChatbotService;
import com.nexus.shop.model.ApiResponse;
import com.nexus.shop.model.chatbot.entity.ChatbotMenuItem;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;
    
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @GetMapping
    public ApiResponse<List<ChatbotMenuItem>> initChatbot() {
        final ApiResponse<List<ChatbotMenuItem>> response = new ApiResponse<>();
        response.setData(ChatbotMenuFactory.build());

        return response;
    }
}
