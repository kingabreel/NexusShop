package com.nexus.shop.model.notification.entity;

import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.notification.dto.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notification")
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private User user;

    private Status status;

    private String redirectLink;

}
