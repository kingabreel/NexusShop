package com.nexus.shop.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.shop.infra.websocket.WebSocketService;
import com.nexus.shop.model.notification.dto.NotificationDto;

@RestController
@RequestMapping("/api/test")
public final class TestController {

    private final WebSocketService webSocketService;

    public TestController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @GetMapping
    public String test() {
        return "Hello, NexusShop!";
    }

    @PostMapping("/send")
    public void send() {

        final NotificationDto message = new NotificationDto(
                0L,
                "TEST",
                "Mensagem de teste do backend",
                "0");

        webSocketService.sendToAll(message);
    }
}
