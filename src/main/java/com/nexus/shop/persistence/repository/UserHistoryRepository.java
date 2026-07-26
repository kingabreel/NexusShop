package com.nexus.shop.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nexus.shop.model.analytic.entity.UserHistory;
import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.product.entity.Product;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, UUID> {
    @Query("""
                select distinct uh.product
                from UserHistory uh
                where uh.user = :user
                order by uh.viewedAt desc
            """)
    List<Product> findHistoryProducts(final User user);

    @Query("""
                select uh.product
                from UserHistory uh
                where uh.user = :user
                order by uh.viewedAt desc
            """)
    List<Product> findTop20HistoryProducts(User user, Pageable pageable);
}
