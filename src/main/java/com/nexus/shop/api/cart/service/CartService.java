package com.nexus.shop.api.cart.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.cart.entity.Cart;
import com.nexus.shop.model.cart.entity.CartItem;
import com.nexus.shop.model.cart.enums.CartStatus;
import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.persistence.repository.CartItemRepository;
import com.nexus.shop.persistence.repository.CartRepository;
import com.nexus.shop.persistence.repository.ProductRepository;
import com.nexus.shop.utils.helpers.AuthenticatedUserHelper;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final AuthenticatedUserHelper authenticatedUserHelper;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
            ProductRepository productRepository, AuthenticatedUserHelper authenticatedUserHelper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.authenticatedUserHelper = authenticatedUserHelper;
    }

    public Cart getCart() {
        User user = authenticatedUserHelper.getAuthenticatedUser();
        return cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Cart not found."));
    }

    public Cart getOrCreateCart() {
        User user = authenticatedUserHelper.getAuthenticatedUser();
        return cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    public Cart addCartItem(UUID productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found."));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock.");
        }

        Cart cart = getOrCreateCart();

        CartItem existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem item = new CartItem(cart, product, quantity, product.getPrice());
            cart.getItems().add(item);
        }

        cart.recalculateTotals();
        return cartRepository.save(cart);
    }

    public Cart updateCartItem(UUID productId, Integer quantity) {
        Cart cart = getCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found."));

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Cart item not found."));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock.");
        }

        cartItem.setQuantity(quantity);
        cart.recalculateTotals();
        cartItemRepository.save(cartItem);
        return cartRepository.save(cart);
    }

    public CartItem getCartItem(UUID productId) {
        Cart cart = getCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found."));

        return cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Cart item not found."));
    }

    public void deleteCartItem(UUID productId) {
        Cart cart = getCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found."));

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Cart item not found."));

        cart.getItems().remove(cartItem);
        cart.recalculateTotals();
        cartRepository.save(cart);
    }

    public void deleteCart() {
        Cart cart = getCart();
        cartRepository.delete(cart);
    }
}
