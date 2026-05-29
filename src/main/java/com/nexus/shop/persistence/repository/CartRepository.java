package com.nexus.shop.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.cart.entity.Cart;
import com.nexus.shop.model.cart.enums.CartStatus;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserAndStatus(User user, CartStatus status);

}
