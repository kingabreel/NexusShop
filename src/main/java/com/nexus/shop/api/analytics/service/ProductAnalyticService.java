package com.nexus.shop.api.analytics.service;

import org.springframework.stereotype.Service;

import com.nexus.shop.model.analytic.entity.ProductAnalytic;
import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.persistence.repository.ProductAnalyticRepository;

@Service
public class ProductAnalyticService {

    private final ProductAnalyticRepository productAnalyticRepository;

    public ProductAnalyticService(final ProductAnalyticRepository repository) {
        this.productAnalyticRepository = repository;
    }

    public void addProductView(final Product product) {
        if (null == product) {
            return;
        }

        final ProductAnalytic analytic = this.getStoredProductAnalytic(product);

        if (null == analytic) {
            this.productAnalyticRepository.save(this.createAnalytic(product));
        } else {
            analytic.incrementViewCount();
            this.productAnalyticRepository.save(analytic);
        }
    }

    private ProductAnalytic createAnalytic(final Product product) {
        final ProductAnalytic analytic = new ProductAnalytic();
        analytic.setProduct(product);
        return analytic;
    }

    public ProductAnalytic getStoredProductAnalytic(final Product product) {
        if (null == product) {
            return null;
        }

        return this.productAnalyticRepository.findByProductId(product.getId())
                .orElse(null);
    }
}
