package com.nexus.shop.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexus.shop.model.auth.entity.RefreshToken;

@Repository
public interface TokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findById(String token);
}
