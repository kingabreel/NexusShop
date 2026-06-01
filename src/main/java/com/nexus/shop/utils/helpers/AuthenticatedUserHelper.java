package com.nexus.shop.utils.helpers;

import org.springframework.stereotype.Component;

import com.nexus.shop.model.auth.entity.User;
import com.nexus.shop.persistence.repository.UserRepository;

@Component
public class AuthenticatedUserHelper {

    private final UserRepository userRepository;

    public AuthenticatedUserHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser() {
        String email = UserContextHelper.getCurrentUserEmail();

        if (email == null) {
            throw new RuntimeException("User not authenticated");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
