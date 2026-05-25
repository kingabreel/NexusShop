package com.nexus.shop.api.cart.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.shop.api.cart.service.CartService;
import com.nexus.shop.model.ApiResponse;
import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.cart.entity.Cart;
import com.nexus.shop.model.cart.entity.CartItem;
import com.nexus.shop.model.cart.request.CartItemCreateDTO;
import com.nexus.shop.model.cart.request.CartItemUpdateDTO;
import com.nexus.shop.model.cart.response.CartItemResponseDTO;
import com.nexus.shop.model.cart.response.CartResponseDTO;
import com.nexus.shop.persistence.repository.UserRepository;
import com.nexus.shop.utils.converters.ConverterUtil;
import com.nexus.shop.utils.helpers.UserContextHelper;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartItemResponseDTO>> createCartItem(
            @RequestBody @Valid CartItemCreateDTO dto) {

        try {
            String email = UserContextHelper.getCurrentUserEmail();
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, "User not authenticated"));
            }
            User user = userRepository.findByEmail(email)
                    .orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, "User not found"));
            }

            Cart cart = cartService.addCartItem(user.getId(), dto.productId(), dto.quantity());

            CartItem item = cart.getItems().stream()
                    .filter(i -> i.getProduct().getId().equals(dto.productId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Item not found after adding"));

            CartItemResponseDTO response = ConverterUtil.toDTO(item);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(response, "Item added to cart"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error"));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDTO>> getCart() {
        try {
            String email = UserContextHelper.getCurrentUserEmail();
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, "User not authenticated"));
            }
            User user = userRepository.findByEmail(email)
                    .orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, "User not found"));
            }

            Cart cart = cartService.getOrCreateCart(user.getId());
            CartResponseDTO response = ConverterUtil.toDTO(cart);

            return ResponseEntity.ok(new ApiResponse<>(response, "Cart retrieved successfully"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error"));
        }
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartItemResponseDTO>> updateCartItem(
            @PathVariable UUID productId,
            @RequestBody @Valid CartItemUpdateDTO dto) {
        try {
            String email = UserContextHelper.getCurrentUserEmail();
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, "User not authenticated"));
            }
            User user = userRepository.findByEmail(email)
                    .orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, "User not found"));
            }

            Cart cart = cartService.getOrCreateCart(user.getId());
            Cart updated = cartService.updateCartItem(cart.getId(), productId, dto.quantity());

            CartItem item = updated.getItems().stream()
                    .filter(i -> i.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            return ResponseEntity.ok(new ApiResponse<>(ConverterUtil.toDTO(item), "Item updated"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error"));
        }
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteCartItem(@PathVariable UUID productId) {
        try {
            String email = UserContextHelper.getCurrentUserEmail();
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, "User not authenticated"));
            }
            User user = userRepository.findByEmail(email)
                    .orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, "User not found"));
            }

            Cart cart = cartService.getOrCreateCart(user.getId());
            cartService.deleteCartItem(cart.getId(), productId);

            return ResponseEntity.ok(new ApiResponse<>(null, "Item removed"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error"));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteCart() {
        try {
            String email = UserContextHelper.getCurrentUserEmail();
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, "User not authenticated"));
            }
            User user = userRepository.findByEmail(email)
                    .orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, "User not found"));
            }

            Cart cart = cartService.getOrCreateCart(user.getId());
            cartService.deleteCart(cart.getId(), user.getId());

            return ResponseEntity.ok(new ApiResponse<>(null, "Cart deleted"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error"));
        }
    }

}
