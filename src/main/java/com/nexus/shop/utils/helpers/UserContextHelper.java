package com.nexus.shop.utils.helpers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import com.nexus.shop.model.auth.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class UserContextHelper {
    private UserContextHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            UserContextHelper.log.warn("No authenticated user found");
            return null;
        }

        final Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            return user;
        }

        UserContextHelper.log.warn("Authenticated principal is not of type User: " + principal.getClass().getName());

        return null;
    }
}
