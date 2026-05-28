package com.nexus.shop.api.recommendation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.persistence.repository.ProductRepository;
import com.nexus.shop.persistence.repository.UserHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserHistoryRepository historyRepository;
    private final ProductRepository productRepository;

    public List<Product> peopleAlsoViewed(final UUID productId) {

        final List<UUID> users = this.historyRepository.findUsersWhoViewed(productId);

        if (users.isEmpty()) {
            return List.of();
        }

        final List<UUID> productIds = this.historyRepository.findProductsViewedByUsers(users, productId);

        return productRepository.findAllById(productIds);
    }
}