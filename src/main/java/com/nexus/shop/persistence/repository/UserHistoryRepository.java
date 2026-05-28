package com.nexus.shop.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nexus.shop.model.analytic.entity.UserHistory;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, UUID> {

    @Query("""
                SELECT DISTINCT uh.userId
                FROM UserHistory uh
                WHERE uh.productId = :productId
                  AND uh.type = 'VIEW'
            """)
    List<UUID> findUsersWhoViewed(@Param("productId") UUID productId);

    @Query("""
                SELECT product_id, COUNT(*) as score
                FROM user_history
                WHERE user_id IN (:users)
                AND product_id != :productId
                AND type = 'VIEW'
                GROUP BY product_id
                ORDER BY score DESC
                LIMIT 10;
            """)
    List<UUID> findProductsViewedByUsers(@Param("users") List<UUID> userIds, @Param("productId") UUID excludeProductId);

    @Query("""
                SELECT product_id, COUNT(*) as score
                FROM user_history
                WHERE product_id != :productId
                AND type = 'PURCHASE'
                AND user_id IN (
                    SELECT DISTINCT user_id
                    FROM user_history
                    WHERE product_id = :productId
                    AND type = 'PURCHASE'
                )
                GROUP BY product_id
                ORDER BY score DESC
                LIMIT 10;
            """)
    List<UUID> findFrequentlyBoughtTogether(@Param("productId") UUID productId);

    @Query("""
                SELECT product_id, COUNT(*) as score
                FROM user_history
                WHERE type = 'PURCHASE'
                GROUP BY product_id
                ORDER BY score DESC
                LIMIT 10;
            """)
    List<UUID> findRankedProducts();
}
