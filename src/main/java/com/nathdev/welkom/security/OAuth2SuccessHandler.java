package com.nathdev.welkom.security;

import com.nathdev.welkom.enums.UserStatus;
import com.nathdev.welkom.models.Profile;
import com.nathdev.welkom.models.User;
import com.nathdev.welkom.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Value("${jwt.expire}")
    private long jwtExpirationTime;

    @Value("${jwt.refreshExpire}")
    private long jwtRefreshExpirationTime;

    @Override
    public void onAuthenticationSuccess(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        assert oAuth2User != null;
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user;
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            // utilisateur existe déjà, on le répère
            user = userOptional.get();
        } else {
            // Premier login ! On crée une nouvelle entité User dans la base

            user = new User();
            user.setEmail(email);

            assert name != null;
            user.setUsername(name.replace(" ", "").toLowerCase());
            user.setRole("WLK_USER");
            user.setPassword(null);
            user.setStatus(UserStatus.ACTIVE);

            Profile profile = new Profile();

            profile.setUser(user);
//            profile.setUsername(name);
//            profile.setEmail(email);
//            profile.setLastLogin(LocalDateTime.now());

            user.setProfile(profile);
            userRepository.save(user);
        }

        // Generation des token
        String accessToken = jwtUtils.generateAccessToken(user.getUsername(), user.getRole(), user.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());

        // injection des cookies
        ResponseCookie accessCookie = jwtUtils.generateCookie("welkom_access", accessToken, jwtExpirationTime);
        ResponseCookie refreshCookie = jwtUtils.generateCookie("welkom_refresh", refreshToken, jwtRefreshExpirationTime);

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        //redirection vers le Frontend
        getRedirectStrategy().sendRedirect(request, response,"http://localhost:4200/dashboard");
    }
}
