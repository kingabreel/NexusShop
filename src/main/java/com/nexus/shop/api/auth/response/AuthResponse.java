package com.nexus.shop.api.auth.response;

import java.time.Instant;

public record AuthResponse(String token, String tokenType, Instant expiresAt) {
}
