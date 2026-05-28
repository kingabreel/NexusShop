package com.nexus.shop.api.auth.service;

import java.util.Set;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.nexus.shop.api.auth.service.impl.RefreshTokenService;
import com.nexus.shop.infra.security.JwtService;
import com.nexus.shop.model.auth.entity.AuthProvider;
import com.nexus.shop.model.auth.entity.RefreshToken;
import com.nexus.shop.model.auth.entity.Role;
import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.model.auth.request.AuthTokens;
import com.nexus.shop.model.auth.request.LoginRequest;
import com.nexus.shop.model.auth.request.RegisterRequest;
import com.nexus.shop.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final RefreshTokenService refreshTokenService;
        private final CustomUserDetailsService userDetailsService;
        private final GoogleTokenServiceImpl googleTokenService;

        @Lazy
        private final AuthenticationManager authenticationManager;

        public AuthTokens login(final LoginRequest request) {

                AuthService.log.info("Init authentication for user: {}", request.email());

                try {
                        final Authentication auth = this.authenticationManager
                                        .authenticate(new UsernamePasswordAuthenticationToken(
                                                        request.email(),
                                                        request.password()));

                        AuthService.log.info("Authentication successful for user: {}", request.email());

                        final UserDetails user = (UserDetails) auth.getPrincipal();

                        final String accessToken = this.jwtService.generateToken(user);

                        final String refreshToken = this.refreshTokenService
                                        .create(user.getUsername());

                        return new AuthTokens(
                                        accessToken,
                                        refreshToken);

                } catch (final Exception e) {

                        AuthService.log.error("Login failed", e);

                        throw e;
                }
        }

        public void register(final RegisterRequest request) {

                if (this.userRepository
                                .findByEmail(request.email())
                                .isPresent()) {

                        throw new RuntimeException(
                                        "User already exists");
                }

                final User user = new User(
                                request.username(),
                                request.email(),
                                this.passwordEncoder.encode(
                                                request.password()),
                                Set.of(Role.CLIENT));

                this.userRepository.save(user);
        }

        public String refresh(final String refreshToken) {
                final RefreshToken token = this.refreshTokenService.validate(refreshToken);

                final UserDetails user = this.userDetailsService.loadUserByUsername(token.getUsername());

                return this.jwtService.generateToken(user);
        }

        public AuthTokens googleLogin(final String googleToken) {

                final GoogleIdToken.Payload payload = this.googleTokenService
                                .verifyToken(googleToken);

                final String email = payload.getEmail();

                final String name = (String) payload.get("name");

                // final String picture = (String) payload.get("picture");

                final User user = this.userRepository
                                .findByEmail(email)
                                .orElseGet(() -> {

                                        final User newUser = new User();

                                        newUser.setEmail(email);

                                        newUser.setUsername(name);

                                        // newUser.setPicture(picture);

                                        newUser.setProvider(AuthProvider.GOOGLE);

                                        return this.userRepository.save(newUser);
                                });

                final String accessToken = this.jwtService.generateToken(user);
                final String refreshToken = this.refreshTokenService.create(user.getEmail());

                return new AuthTokens(
                                accessToken,
                                refreshToken);
        }
}
