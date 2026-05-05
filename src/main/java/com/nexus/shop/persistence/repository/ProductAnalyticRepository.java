package com.nexus.shop.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexus.shop.model.analytic.entity.ProductAnalytic;

@Repository
public interface ProductAnalyticRepository extends JpaRepository<ProductAnalytic, Long> {
    Optional<ProductAnalytic> findByProductId(Long productId);
}
