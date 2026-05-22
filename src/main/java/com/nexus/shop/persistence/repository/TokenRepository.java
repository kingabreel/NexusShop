package com.nexus.shop.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexus.shop.model.auth.entity.Token;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    Optional<Token> findByToken(String token);
}
