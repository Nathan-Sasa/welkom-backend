package com.nathdev.welkom.services;

//import com.nathdev.welkom.dto.LoginRequest;
//import com.nathdev.welkom.dto.RegisterRequest;
//import com.nathdev.welkom.dto.user.AuthResponseDto;
//import com.nathdev.welkom.enums.UserStatus;
//import com.nathdev.welkom.models.Profile;
import com.nathdev.welkom.models.User;
import com.nathdev.welkom.repositories.UserRepository;
//import com.nathdev.welkom.security.CustomDetailsService;
import com.nathdev.welkom.security.JwtUtils;
//import com.nimbusds.openid.connect.sdk.LogoutRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestBody;
//
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
//import java.util.NoSuchElementException;
//import java.util.Optional;

@Slf4j
@Service
//@AllArgsConstructor
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;


    public ResponseEntity<?> currentUser(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromCookies(request, "welkom_access");

        if (token != null && !jwtUtils.isTokenExpire(token)) {
            String usernameOrEmail = jwtUtils.extractUsername(token);

            return userRepository.findByEmail(usernameOrEmail)
                    .or(() -> userRepository.findByUsername(usernameOrEmail))
                    .map(user -> {
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("username", user.getUsername());
                        userData.put("email", user.getEmail());
                        userData.put("role", user.getRole());

                        if (user.getProfile() != null) {
                            userData.put("first_name", user.getProfile().getFirstName());
                            userData.put("avatar", user.getProfile().getAvatar());
                        }

                        return ResponseEntity.ok(userData);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Non connecté"));
    }
//
//    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
//        String refreshToken = jwtUtils.getJwtFromCookies(request, "welkom_refresh");
//
//        if (refreshToken != null && !jwtUtils.isTokenExpire(refreshToken)) {
//            String username = jwtUtils.extractUsername(refreshToken);
//            UserDetails userDetails = customDetailsService.loadUserByUsername(username);
//
//            Optional<User> user = userRepository.findByEmail(username);
//
//            if (user.isPresent()) {
//                if (jwtUtils.validateToken(refreshToken, userDetails.getUsername())) {
//                    String newAccessToken = jwtUtils.generateAccessToken(userDetails.getUsername(), user.get().getRole(), null);
//                    ResponseCookie newAccessCookie = jwtUtils.generateCookie("welkom_access", newAccessToken, jwtExpirationTime);
//                    response.addHeader(HttpHeaders.SET_COOKIE, newAccessCookie.toString());
//                    return ResponseEntity.ok(Map.of("message", "Token mis à jour."));
//                }
//            }
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Jeton de rafraîchissement invalide ou expiré."));
//    }
//
    public ResponseEntity<?> logout (HttpServletResponse response) {
        ResponseCookie cleanAccess = jwtUtils.getCleanCookie("welkom_access");
        ResponseCookie cleanRefresh = jwtUtils.getCleanCookie("welkom_refresh");

        response.addHeader(HttpHeaders.SET_COOKIE, cleanAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cleanRefresh.toString());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Utilisateur déconnecté"));
    }






//    public User getUserAuth(HttpServletRequest request) {
//        String jwtToken = jwtUtils.getJwtFromCookies(request, "welkom_access");
////        String username = null;
//
//        if (jwtToken != null) {
//            try {
//                String username = jwtUtils.extractUsername(jwtToken);
//                return userRepository.findByEmail(username)
//                        .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
//            } catch (Exception e) {
//                log.error("JWT validation link failed{}", e.getMessage());
//            }
//        }
//        return null;
//    }
//
//    public void updateLastLoginTime(HttpServletRequest request) {
//        User user = getUserAuth(request);
//        user.setLastLogin(LocalDateTime.now());
//        userRepository.save(user);
//    }
}
