package com.nexus.shop.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nexus.shop.model.rating.entity.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {

    List<Rating> findByProduct_Id(UUID productId);

    boolean existsByProduct_IdAndUser_Id(UUID productId, UUID userId);

    @Query("""
                SELECT AVG(r.rating)
                FROM Rating r
                WHERE r.product.id = :productId
            """)
    Double findAverageRatingByProductId(UUID productId);

    @Query("""
                SELECT COUNT(r)
                FROM Rating r
                WHERE r.product.id = :productId
            """)
    Long countByProduct_Id(UUID productId);
}
