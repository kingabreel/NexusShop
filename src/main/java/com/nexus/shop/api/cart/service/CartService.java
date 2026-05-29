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
import com.nexus.shop.persistence.repository.UserRepository;

@Service
public class CartService {
        private final CartRepository cartRepository;
        private final CartItemRepository cartItemRepository;
        private final ProductRepository productRepository;
        private final UserRepository userRepository;

        public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                        ProductRepository productRepository, UserRepository userRepository) {
                this.cartRepository = cartRepository;
                this.cartItemRepository = cartItemRepository;
                this.productRepository = productRepository;
                this.userRepository = userRepository;
        }

        public Cart getOrCreateCart(UUID userId) {
                User user = this.userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found."));

                return cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
                                .orElseGet(() -> cartRepository.save(new Cart(user)));
        }

        public Cart addCartItem(UUID userId, UUID productId, Integer quantity) {
                User user = this.userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found."));

                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new RuntimeException("Product not found."));

                if (product.getStock() < quantity) {
                        throw new RuntimeException("Insufficient stock.");
                }

                Cart cart = getOrCreateCart(user.getId());

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

        public void deleteCartItem(UUID cartId, UUID productId) {
                Cart cart = cartRepository.findById(cartId)
                                .orElseThrow(() -> new RuntimeException("Cart not found."));

                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new RuntimeException("Product not found."));

                CartItem cartItem = cartItemRepository
                                .findByCartAndProduct(cart, product)
                                .orElseThrow(() -> new RuntimeException("Cart item not found."));

                cart.getItems().remove(cartItem);

                cart.recalculateTotals();

                cartRepository.save(cart);
        }

        public Cart updateCartItem(UUID cartId, UUID productId, Integer quantity) {
                Cart cart = cartRepository.findById(cartId)
                                .orElseThrow(() -> new RuntimeException("Cart not found."));

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

        public CartItem getCartItem(UUID cartId, UUID productId) {
                Cart cart = cartRepository.findById(cartId)
                                .orElseThrow(() -> new RuntimeException("Cart not found."));

                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new RuntimeException("Product not found."));
                return cartItemRepository
                                .findByCartAndProduct(cart, product)
                                .orElseThrow(() -> new RuntimeException("Cart item not found."));

        }

        public void deleteCart(UUID cartId, UUID userId) {
                Cart cart = cartRepository.findById(cartId)
                                .orElseThrow(() -> new RuntimeException("Cart not found."));

                User user = cart.getUser();

                if (!user.getId().equals(userId)) {
                        throw new RuntimeException("Cart does not belong to this user.");
                }
                cartRepository.delete(cart);
        }
}
