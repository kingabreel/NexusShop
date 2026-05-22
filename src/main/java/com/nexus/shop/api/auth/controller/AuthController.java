package com.nexus.shop.api.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.shop.api.auth.response.AuthResponse;
import com.nexus.shop.api.auth.service.AuthService;
import com.nexus.shop.model.auth.request.LoginRequest;
import com.nexus.shop.model.auth.request.RegisterRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthController.log.info("Init login");
        String token = this.authService.login(request);
        AuthController.log.info("Login success");
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody final RegisterRequest request) {
        this.authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

}
