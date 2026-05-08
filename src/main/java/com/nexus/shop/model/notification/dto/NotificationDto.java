package com.nexus.shop.model.notification.dto;

public record NotificationDto(
                Long id,
                String status,
                String message,
                String userId) {
}
