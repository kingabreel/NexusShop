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
import com.nexus.shop.model.cart.entity.Cart;
import com.nexus.shop.model.cart.entity.CartItem;
import com.nexus.shop.model.cart.request.CartItemCreateDTO;
import com.nexus.shop.model.cart.request.CartItemUpdateDTO;
import com.nexus.shop.model.cart.response.CartItemResponseDTO;
import com.nexus.shop.model.cart.response.CartResponseDTO;
import com.nexus.shop.utils.converters.ConverterUtil;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartItemResponseDTO>> createCartItem(
            @RequestBody @Valid CartItemCreateDTO dto) {
        try {
            Cart cart = cartService.addCartItem(dto.productId(), dto.quantity());

            CartItem item = cart.getItems().stream()
                    .filter(i -> i.getProduct().getId().equals(dto.productId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Item not found after adding"));

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(ConverterUtil.toDTO(item), "Item added to cart"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (RuntimeException e) {
            if (e.getMessage() != null &&
                    (e.getMessage().equals("User not authenticated") ||
                            e.getMessage().equals("User not found"))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, e.getMessage()));
            }
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
            Cart cart = cartService.getOrCreateCart();
            return ResponseEntity.ok(new ApiResponse<>(ConverterUtil.toDTO(cart), "Cart retrieved successfully"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (RuntimeException e) {
            if (e.getMessage() != null &&
                    (e.getMessage().equals("User not authenticated") ||
                            e.getMessage().equals("User not found"))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error"));
        }
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartItemResponseDTO>> updateCartItem(
            @PathVariable UUID productId,
            @RequestBody @Valid CartItemUpdateDTO dto) {
        try {
            Cart updated = cartService.updateCartItem(productId, dto.quantity());

            CartItem item = updated.getItems().stream()
                    .filter(i -> i.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            return ResponseEntity.ok(new ApiResponse<>(ConverterUtil.toDTO(item), "Item updated"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (RuntimeException e) {
            if (e.getMessage() != null &&
                    (e.getMessage().equals("User not authenticated") ||
                            e.getMessage().equals("User not found"))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error"));
        }
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteCartItem(@PathVariable UUID productId) {
        try {
            cartService.deleteCartItem(productId);
            return ResponseEntity.ok(new ApiResponse<>(null, "Item removed"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (RuntimeException e) {
            if (e.getMessage() != null &&
                    (e.getMessage().equals("User not authenticated") ||
                            e.getMessage().equals("User not found"))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error"));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteCart() {
        try {
            cartService.deleteCart();
            return ResponseEntity.ok(new ApiResponse<>(null, "Cart deleted"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (RuntimeException e) {
            if (e.getMessage() != null &&
                    (e.getMessage().equals("User not authenticated") ||
                            e.getMessage().equals("User not found"))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(null, e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error"));
        }
    }
}
