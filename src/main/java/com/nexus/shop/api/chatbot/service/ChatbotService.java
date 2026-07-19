package com.nexus.shop.api.chatbot.service;

import org.springframework.stereotype.Service;

import com.nexus.shop.model.chatbot.request.ChatbotRequestDto;

@Service
public class ChatbotService {
    
    public Object processRequest(final ChatbotRequestDto requestDto) {

        
        return "Processed request for option: " + requestDto.option() + ", subOption: " + requestDto.subOption();
    }
}
