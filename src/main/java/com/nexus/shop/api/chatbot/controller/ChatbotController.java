package com.nexus.shop.api.chatbot.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.shop.api.chatbot.service.ChatbotMenuFactory;
import com.nexus.shop.api.chatbot.service.ChatbotService;
import com.nexus.shop.model.ApiResponse;
import com.nexus.shop.model.chatbot.entity.ChatbotMenuItem;
import com.nexus.shop.model.chatbot.request.ChatbotRequestDto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

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

    @PostMapping
    public ApiResponse<Object> getInformation(@RequestBody final ChatbotRequestDto requestDto) {
        final ApiResponse<Object> response = new ApiResponse<>();

        response.setData(this.chatbotService.process(requestDto));

        return response;
    }
}
