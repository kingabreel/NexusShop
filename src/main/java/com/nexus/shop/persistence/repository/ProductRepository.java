package com.nexus.shop.persistence.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.product.enums.Category;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Page<Product> findByHighlight(boolean highlight, Pageable pageable);

    @Query(value = """
            SELECT *
            FROM product
            ORDER BY embedding <-> :embedding
            LIMIT :limit
            """, nativeQuery = true)
    List<Product> searchSimilar(@Param("embedding") float[] embedding,
            @Param("limit") int limit);

    List<Product> findByHighlightTrue();

    List<Product> findByNameContainingIgnoreCase(String name);

    Optional<Product> findByNameIgnoreCase(String name);

    List<Product> findByCategory(Category category);

    List<Product> findByCategoryAndStockGreaterThan(Category category, Integer stock);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    List<Product> findByPriceLessThanEqual(BigDecimal max);

    List<Product> findByPriceGreaterThanEqual(BigDecimal min);

    List<Product> findByStockGreaterThan(Integer stock);

    List<Product> findByStockEquals(Integer stock);

    List<Product> findTop10ByCategoryAndIdNot(Category category, UUID id);

    List<Product> findTop10ByCategoryAndHighlightTrue(Category category);

    List<Product> findTop10ByOrderByPriceAsc();

    List<Product> findTop10ByOrderByPriceDesc();

    List<Product> findTop10ByOrderByCreatedAtDesc();

    @Query("""
                select p
                from Product p
                join Rating r on r.product = p
                group by p
                order by avg(r.rating) desc
            """)
    List<Product> findTopRated(Pageable pageable);
}
