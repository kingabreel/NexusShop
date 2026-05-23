package com.nexus.shop.utils.helpers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import com.nexus.shop.model.auth.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class UserContextHelper {
    private UserContextHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static String getCurrentUserEmail() {

        final Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            UserContextHelper.log.warn(
                    "No authenticated user found");

            return null;
        }

        final Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt jwt) {

            return jwt.getSubject();
        }

        if (principal instanceof User user) {

            return user.getUsername();
        }

        UserContextHelper.log.warn(
                "Unsupported principal type: {}",
                principal.getClass().getName());

        return null;
    }
}
