package com.nexus.shop.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexus.shop.model.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Page<Product> findByHighlight(boolean highlight, Pageable pageable);

    @Query(value = """
    SELECT * FROM product
    ORDER BY embedding <-> CAST(:embedding AS vector)
    LIMIT 10
    """, nativeQuery = true)
    List<Product> searchSimilar(@Param("embedding") final String embedding);
}
