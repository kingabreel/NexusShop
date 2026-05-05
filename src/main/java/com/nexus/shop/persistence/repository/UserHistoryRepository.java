package com.nexus.shop.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexus.shop.model.analytic.entity.UserHistory;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    
}
