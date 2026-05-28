package com.nexus.shop.api.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Service
public class GoogleTokenServiceImpl {

    @Value("${registration.google.client-id}")
    private String googleClientId;

    public GoogleIdToken.Payload verifyToken(final String token) {

        try {
            final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(List.of(googleClientId))
                    .build();

            final GoogleIdToken googleIdToken = verifier.verify(token);

            if (googleIdToken == null) {
                throw new RuntimeException("Invalid Google token");
            }

            return googleIdToken.getPayload();

        } catch (final Exception e) {
            throw new RuntimeException("Google token validation failed");
        }
    }
}
