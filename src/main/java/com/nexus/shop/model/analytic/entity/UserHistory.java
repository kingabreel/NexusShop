package com.nexus.shop.model.analytic.entity;

import java.time.LocalDateTime;

import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.product.entity.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_history")
@Getter
@Setter
@NoArgsConstructor
public class UserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private User user;

    private Product product;

    private LocalDateTime viewedAt = LocalDateTime.now();

}
