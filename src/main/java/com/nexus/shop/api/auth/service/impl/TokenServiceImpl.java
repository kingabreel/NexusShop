package com.nexus.shop.api.auth.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.nexus.shop.api.auth.service.TokenService;
import com.nexus.shop.model.auth.entity.Token;
import com.nexus.shop.persistence.repository.TokenRepository;

@Service
public class TokenServiceImpl implements TokenService {

    private final JwtEncoder jwtEncoder;
    private final TokenRepository tokenRepository;

    @Value("${jwt.expiration-seconds:3600}")
    private long jwtExpirationSeconds;

    public TokenServiceImpl(JwtEncoder jwtEncoder, TokenRepository tokenRepository) {
        this.jwtEncoder = jwtEncoder;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public String createToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(jwtExpirationSeconds);

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("nexus-shop")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(authentication.getName())
                .claim("roles", roles)
                .build();

        String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        Token token = new Token(tokenValue, "BEARER", authentication.getName(), now, expiresAt);
        tokenRepository.save(token);

        return tokenValue;
    }

    @Override
    public java.util.Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
}
