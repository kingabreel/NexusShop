package com.nexus.shop.api.auth.service;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexus.shop.infra.security.JwtService;
import com.nexus.shop.model.auth.entity.Role;
import com.nexus.shop.model.auth.entity.User;
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
    private final AuthenticationManager authenticationManager;

    public String login(final LoginRequest request) {

        AuthService.log.info("Init authentication for user: {}", request.username());

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()));

            AuthService.log.info("Authentication successful for user: {}", request.username());
            return jwtService.generateToken((UserDetails) auth.getPrincipal());

        } catch (Exception e) {
            AuthService.log.error("Login failed", e);
            throw e;
        }
    }

    public void register(final RegisterRequest request) {

        if (this.userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        final User user = new User(
                request.username(),
                this.passwordEncoder.encode(request.password()),
                Set.of(Role.CLIENT));

        this.userRepository.save(user);
    }

}
