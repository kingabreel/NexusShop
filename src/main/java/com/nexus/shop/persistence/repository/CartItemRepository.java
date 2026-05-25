package com.nexus.shop.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexus.shop.model.cart.entity.Cart;
import com.nexus.shop.model.cart.entity.CartItem;
import com.nexus.shop.model.product.entity.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
