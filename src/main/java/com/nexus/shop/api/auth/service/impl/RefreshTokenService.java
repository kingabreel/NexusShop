package com.nexus.shop.api.auth.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.nexus.shop.model.auth.entity.RefreshToken;
import com.nexus.shop.persistence.repository.TokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenRepository repository;

    public String create(String username) {

        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .username(username)
                .expiresAt(
                        Instant.now()
                                .plus(7, ChronoUnit.DAYS))
                .revoked(false)
                .build();

        repository.save(refreshToken);

        return token;
    }

    public RefreshToken validate(String token) {

        RefreshToken refreshToken = repository.findById(token)
                .orElseThrow();

        if (refreshToken.isRevoked()) {
            throw new RuntimeException(
                    "Refresh token revoked");
        }

        if (refreshToken.getExpiresAt()
                .isBefore(Instant.now())) {

            throw new RuntimeException(
                    "Refresh token expired");
        }

        return refreshToken;
    }

    public void revoke(String token) {

        repository.findById(token)
                .ifPresent(refreshToken -> {

                    refreshToken.setRevoked(true);

                    repository.save(refreshToken);
                });
    }
}
