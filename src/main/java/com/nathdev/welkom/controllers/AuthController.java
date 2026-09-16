package com.nathdev.welkom.controllers;

import com.nathdev.welkom.dto.LoginRequest;
import com.nathdev.welkom.dto.RegisterRequest;
import com.nathdev.welkom.dto.user.AuthResponseDto;
import com.nathdev.welkom.enums.UserStatus;
import com.nathdev.welkom.models.Profile;
import com.nathdev.welkom.models.User;
import com.nathdev.welkom.repositories.UserRepository;
import com.nathdev.welkom.security.CustomDetailsService;
import com.nathdev.welkom.security.JwtUtils;
import com.nathdev.welkom.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentifaction", description = "Endpoints pour enregistrer, connecter et modifier un utilisateur.")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CustomDetailsService customDetailsService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${jwt.expire}")
    private long jwtExpirationTime;

    @Value("${jwt.refreshExpire}")
    private long jwtRefreshExpire;

    //1. REGISTER
//    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
//        try {
//            return userService.register(registerRequest, response);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
    @PostMapping("/register")
    @Operation(summary = "S'enregistrer")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
        try {
            if (userRepository.findByUsername(registerRequest.username()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Ce nom d'utilisateur est déjà utilisé."));
            }

            if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Cet e-mail est déjà utilisé."));
            }

            User user = new User();
            user.setUsername(registerRequest.username());
            user.setEmail(registerRequest.email());
            user.setRole("WLK_USER");
            user.setPassword(passwordEncoder.encode(registerRequest.password()));

            Profile profile = new Profile();
            profile.setUser(user);
//            profile.setEmail(user.getEmail());
//            profile.setUsername(user.getUsername());
//            profile.setLastLogin(LocalDateTime.now());

            user.setProfile(profile);
            userRepository.save(user);

            // Génération des tokens
            String accessToken = jwtUtils.generateAccessToken(user.getUsername(), "ROLE_" + user.getRole(), user.getEmail());
            String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());

            // Injection des cookies sécurisés
            ResponseCookie accessCookie = jwtUtils.generateCookie("welkom_access", accessToken, jwtExpirationTime);
            ResponseCookie refreshCookie = jwtUtils.generateCookie("welkom_refresh", refreshToken, jwtRefreshExpire);

            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

            return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponseDto.fromEntity(user));
        }
        catch (Exception e) {
            log.error("Erreur lors du register : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Une erreur est survenue lors de l'inscription."));
        }
    }

//     2. LOGIN
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
//        try {
////             Spring Security valide ou lève directement une exception si l'authentification échoue
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
//            );
//            return userService.login(loginRequest, response);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
    @PostMapping("/login")
    @Operation(summary = "Se connecter")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        try {
            // Spring Security valide ou lève directement une exception si l'authentification échoue
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
            );


            User user = userRepository.findByEmail(loginRequest.email())
                    .orElseThrow(() -> new NoSuchElementException("Aucun utilisateur trouvé avec l'e-mail : " + loginRequest.email()));

            // Génération des tokens
            String accessToken = jwtUtils.generateAccessToken(user.getUsername(), user.getRole(), user.getEmail());
            String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());

            // Injection des cookies sécurisés
            ResponseCookie accessCookie = jwtUtils.generateCookie("welkom_access", accessToken, jwtExpirationTime);
            ResponseCookie refreshCookie = jwtUtils.generateCookie("welkom_refresh", refreshToken, jwtRefreshExpire);

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

            Map<String, Object> authLoginData = new HashMap<>();
            authLoginData.put("username", user.getUsername());
            authLoginData.put("email", user.getEmail());
            authLoginData.put("role", user.getRole());
            authLoginData.put("first_name", user.getProfile().getFirstName());
            authLoginData.put("avatar", user.getProfile().getAvatar());

//            user.getProfile().setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK).body(authLoginData);
//            return ResponseEntity.status(HttpStatus.OK).body(AuthResponseDto.fromEntity(user));
        }
        catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "E-mail ou mot de passe incorrect."));
        }
    }

    //3. REFRESH
//    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
//        return userService.refreshToken(request, response);
//    }
    @PostMapping("/refresh")
    @Operation(summary = "Rafraichir l'accèss")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtils.getJwtFromCookies(request, "welkom_refresh");

        if (refreshToken != null && !jwtUtils.isTokenExpire(refreshToken)) {
            String username = jwtUtils.extractUsername(refreshToken);
            UserDetails userDetails = customDetailsService.loadUserByUsername(username);

            Optional<User> user = userRepository.findByEmail(username);

            if (user.isPresent()) {
                if (jwtUtils.validateToken(refreshToken, userDetails.getUsername())) {
                    String newAccessToken = jwtUtils.generateAccessToken(userDetails.getUsername(), user.get().getRole(), null);
                    ResponseCookie newAccessCookie = jwtUtils.generateCookie("welkom_access", newAccessToken, jwtExpirationTime);
                    response.addHeader(HttpHeaders.SET_COOKIE, newAccessCookie.toString());
                    return ResponseEntity.ok(Map.of("message", "Token mis à jour."));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Jeton de rafraîchissement invalide ou expiré."));
    }

    // 4. DÉCONNEXION
    @PostMapping("/logout")
    @Operation(summary = "Se déconnecter")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        return userService.logout(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Qui suis-je ?")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        return userService.currentUser(request);
    }
}