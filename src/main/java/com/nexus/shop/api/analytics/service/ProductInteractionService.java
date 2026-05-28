package com.nexus.shop.api.analytics.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.nexus.shop.model.analytic.entity.InteractionType;
import com.nexus.shop.model.analytic.entity.UserHistory;
import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.persistence.repository.ProductAnalyticRepository;
import com.nexus.shop.persistence.repository.UserHistoryRepository;
import com.nexus.shop.persistence.repository.UserRepository;
import com.nexus.shop.utils.helpers.UserContextHelper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductInteractionService {

    private final UserHistoryRepository userHistoryRepository;
    private final ProductAnalyticRepository productAnalyticRepository;
    private final UserRepository userRepository;

    public void registerView(final Product product) {
        final String email = UserContextHelper.getCurrentUserEmail();

        final User user = userRepository.findByEmail(email).orElse(null);

        if (product == null || user == null)
            return;

        this.saveHistory(product, user, InteractionType.VIEW);
        this.productAnalyticRepository.incrementView(product.getId());
    }

    public void registerClick(final Product product) {
        final String email = UserContextHelper.getCurrentUserEmail();

        final User user = userRepository.findByEmail(email).orElse(null);

        if (product == null || user == null)
            return;

        this.saveHistory(product, user, InteractionType.CLICK);
    }

    private void saveHistory(final Product product, final User user, final InteractionType type) {
        final UserHistory history = new UserHistory();
        history.setProduct(product);
        history.setUser(user);
        history.setType(type);
        history.setCreatedAt(LocalDateTime.now());

        this.userHistoryRepository.save(history);
    }
}