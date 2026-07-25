package com.nexus.shop.utils.converters;

import com.nexus.shop.model.product.response.ProductResponseDTO;
import com.nexus.shop.model.rating.entity.Rating;
import com.nexus.shop.model.rating.response.RatingResponseDTO;

import java.util.List;

import com.nexus.shop.model.cart.entity.Cart;
import com.nexus.shop.model.cart.entity.CartItem;
import com.nexus.shop.model.cart.response.CartItemResponseDTO;
import com.nexus.shop.model.cart.response.CartResponseDTO;
import com.nexus.shop.model.product.entity.Product;

public final class ConverterUtil {

    private ConverterUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ProductResponseDTO toDTO(
            Product product,
            Double averageRating,
            Long ratingCount) {

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.isHighlight(),
                averageRating,
                ratingCount);
    }

    public static CartItemResponseDTO toDTO(CartItem item) {
        return new CartItemResponseDTO(
                item.getCart().getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPriceAtTime(),
                item.getTotal());
    }

    public static CartResponseDTO toDTO(Cart cart) {
        List<CartItemResponseDTO> items = cart.getItems().stream()
                .map(ConverterUtil::toDTO)
                .toList();

        return new CartResponseDTO(
                cart.getId(),
                items,
                cart.getSubtotal(),
                cart.getDiscount(),
                cart.getTotal());
    }

    public static RatingResponseDTO toDTO(Rating rating) {
        String name = rating.isAnonymous() ? null : rating.getUser().getUsername();
        return new RatingResponseDTO(
                rating.getId(),
                rating.getRating(),
                rating.getComment(),
                rating.getProduct().getId(),
                name,
                rating.isAnonymous(),
                rating.getImageUrl());
    }
}
