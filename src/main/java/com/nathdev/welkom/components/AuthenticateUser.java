package com.nathdev.welkom.components;

import com.nathdev.welkom.models.User;
import com.nathdev.welkom.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticateUser {

    private final UserRepository userRepository;

    public User getUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        String user = authentication.getName();

        return userRepository.findByEmail(user)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

}
