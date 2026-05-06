package com.nexus.shop.infra.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.nexus.shop.model.notification.dto.NotificationDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private static final String TOPIC_NOTIFICATIONS = "/topic/notifications";
    private static final String QUEUE_USER_NOTIFICATIONS = "/queue/notifications";

    private final SimpMessagingTemplate messagingTemplate;

    public void sendToAll(final NotificationDto message) {
        this.messagingTemplate.convertAndSend(TOPIC_NOTIFICATIONS, message);
    }

    public void sendToUser(final String userId, final NotificationDto message) {
        this.messagingTemplate.convertAndSendToUser(
                userId,
                QUEUE_USER_NOTIFICATIONS,
                message
        );
    }
}