package com.nexus.shop.api.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.shop.api.auth.request.GoogleLoginRequest;
import com.nexus.shop.api.auth.response.AuthResponse;
import com.nexus.shop.api.auth.service.AuthService;
import com.nexus.shop.model.auth.request.AuthTokens;
import com.nexus.shop.model.auth.request.LoginRequest;
import com.nexus.shop.model.auth.request.RegisterRequest;

import jakarta.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

        private final AuthService authService;

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(
                        @RequestBody LoginRequest request,
                        HttpServletResponse response) {

                AuthController.log.info("Init login");

                final AuthTokens tokens = this.authService.login(request);

                final ResponseCookie cookie = ResponseCookie.from(
                                "refreshToken",
                                tokens.refreshToken())
                                .httpOnly(true)
                                .secure(false)
                                .sameSite("Lax")
                                .path("/api/auth/refresh")
                                .maxAge(Duration.ofDays(7))
                                .build();

                response.addHeader(
                                HttpHeaders.SET_COOKIE,
                                cookie.toString());

                AuthController.log.info("Login success");

                return ResponseEntity.ok(
                                new AuthResponse(tokens.accessToken()));
        }

        @PostMapping("/register")
        public ResponseEntity<String> register(@RequestBody final RegisterRequest request) {
                this.authService.register(request);
                return ResponseEntity.ok("User registered successfully");
        }

        @PostMapping("/refresh")
        public ResponseEntity<AuthResponse> refresh(
                        @CookieValue("refreshToken") String refreshToken) {

                final String accessToken = this.authService.refresh(refreshToken);

                return ResponseEntity.ok(
                                new AuthResponse(accessToken));
        }

        @PostMapping("/google")
        public ResponseEntity<AuthResponse> googleLogin(
                        @RequestBody GoogleLoginRequest request,
                        HttpServletResponse response) {

                final AuthTokens tokens = this.authService.googleLogin(request.token());

                final ResponseCookie cookie = ResponseCookie.from(
                                "refreshToken",
                                tokens.refreshToken())
                                .httpOnly(true)
                                .secure(false)
                                .sameSite("Lax")
                                .path("/api/auth/refresh")
                                .maxAge(Duration.ofDays(7))
                                .build();

                response.addHeader(
                                HttpHeaders.SET_COOKIE,
                                cookie.toString());

                return ResponseEntity.ok(
                                new AuthResponse(tokens.accessToken()));
        }
}
