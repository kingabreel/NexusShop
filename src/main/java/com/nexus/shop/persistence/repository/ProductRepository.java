package com.nexus.shop.persistence.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexus.shop.model.product.entity.Product;

import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Page<Product> findByHighlight(boolean highlight, Pageable pageable);

    @Modifying
    @Transactional
    @Query("""
                update Product p
                set p.viewCount = p.viewCount + 1
                where p.id = :productId
            """)
    void incrementView(@Param("productId") UUID productId);

    @Modifying
    @Transactional
    @Query("""
                update Product p
                set p.purchaseCount = p.purchaseCount + 1
                where p.id = :productId
            """)
    void incrementPurchase(@Param("productId") UUID productId);

    @Modifying
    @Transactional
    @Query("""
                update Product p
                set p.favoriteCount = p.favoriteCount + 1
                where p.id = :productId
            """)
    void incrementFavorite(@Param("productId") UUID productId);

    @Modifying
    @Transactional
    @Query("""
                update Product p
                set p.cartAddCount = p.cartAddCount + 1
                where p.id = :productId
            """)
    void incrementCartAdd(@Param("productId") UUID productId);

}
