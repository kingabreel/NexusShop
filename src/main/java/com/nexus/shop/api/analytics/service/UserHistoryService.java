package com.nexus.shop.api.analytics.service;

import org.springframework.stereotype.Service;

import com.nexus.shop.model.analytic.entity.UserHistory;
import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.persistence.repository.UserHistoryRepository;
import com.nexus.shop.utils.helpers.UserContextHelper;

@Service
public class UserHistoryService {
    
    private final UserHistoryRepository repository;
    
    public UserHistoryService(final UserHistoryRepository repository) {
        this.repository = repository;
    }

    public void addProductView(final Product product, final User user) {
        if (null == product || null == user) {
            return;
        }

        this.repository.save(this.createAnalytic(product, user));
    }

    public void addProductView(final Product product) {
        final User user = UserContextHelper.getCurrentUser();

        if (null == product || null == user) {
            return;
        }

        this.repository.save(this.createAnalytic(product, user));
    }

    private UserHistory createAnalytic(final Product product, final User user) {
        final UserHistory analytic = new UserHistory();
        analytic.setProduct(product);
        analytic.setUser(user);
        return analytic;
    }
}
