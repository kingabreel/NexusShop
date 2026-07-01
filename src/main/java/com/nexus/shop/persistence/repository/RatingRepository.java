package com.nexus.shop.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.rating.entity.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {
    
    List<Rating> findByProduct(Product product);

    boolean existsByProductIdAndUserId(UUID productId, UUID userId);
}
