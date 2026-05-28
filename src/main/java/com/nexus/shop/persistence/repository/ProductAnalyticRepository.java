package com.nexus.shop.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nexus.shop.model.analytic.entity.ProductAnalytic;

import jakarta.transaction.Transactional;

@Repository
public interface ProductAnalyticRepository extends JpaRepository<ProductAnalytic, UUID> {
    Optional<ProductAnalytic> findByProductId(final UUID productId);

    @Modifying
    @Transactional
    @Query("""
                update ProductAnalytic p
                set p.viewCount = p.viewCount + 1
                where p.product.id = :productId
            """)
    void incrementView(@Param("productId") UUID productId);

}
