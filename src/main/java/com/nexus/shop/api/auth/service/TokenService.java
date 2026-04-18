package com.nexus.shop.api.auth.service;

import java.util.Optional;

import org.springframework.security.core.Authentication;

import com.nexus.shop.model.auth.entity.Token;

public interface TokenService {
    String createToken(Authentication authentication);

    Optional<Token> findByToken(String token);
}
