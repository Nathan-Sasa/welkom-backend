package com.nathdev.welkom.controllers;

import com.nathdev.welkom.dto.LoginRequest;
import com.nathdev.welkom.dto.RegisterRequest;
import com.nathdev.welkom.models.User;
import com.nathdev.welkom.repositories.UserRepository;
import com.nathdev.welkom.security.JwtUtils;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Value("${jwt.expire}")
    private long jwtExpirationTime;

    @Value("${jwt.refreshExpire}")
    private long jwtRefreshExpire;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // Utilisation de structures Map.of pour renvoyer du JSON strict et éviter les conflits CORS
            if (userRepository.findByUsername(registerRequest.username()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Ce nom d'utilisateur est déjà utilisé."));
            }

            if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Cet e-mail est déjà utilisé."));
            }

            User user = new User();
            user.setUsername(registerRequest.username());
            user.setEmail(registerRequest.email());
            user.setRole("USER");
            user.setPassword(passwordEncoder.encode(registerRequest.password()));

            userRepository.save(user);

            Map<String, Object> userAuth = new HashMap<>();
            userAuth.put("username", registerRequest.username());
            userAuth.put("email", registerRequest.email());
            userAuth.put("role", user.getRole().toString());

            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        catch (Exception e) {
            log.error("Erreur lors du register : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Une erreur est survenue lors de l'inscription."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            // Spring Security valide ou lève directement une exception si l'authentification échoue
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // On récupère le modèle User correspondant à l'e-mail validé
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

            // Génération des tokens
            String accessToken = jwtUtils.generateAccessToken(user.getUsername(), "ROLE_" + user.getRole(), user.getEmail());
            String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());

            // Injection des cookies sécurisés
            ResponseCookie accessCookie = jwtUtils.generateCookie("welkom_access", accessToken, jwtExpirationTime);
            ResponseCookie refreshCookie = jwtUtils.generateCookie("welkom_refresh", refreshToken, jwtRefreshExpire);

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

            Map<String, Object> authData = new HashMap<>();
            authData.put("username", user.getUsername());
            authData.put("email", user.getEmail());
            authData.put("role", user.getRole());

            return ResponseEntity.ok(authData);
        }
        catch (AuthenticationException e) {
            log.error("Erreur d'authentification : {}", e.getMessage());
            // Retourner impérativement un format JSON structuré pour maintenir la politique CORS
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "E-mail ou mot de passe incorrect."));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtils.getJwtFromCookies(request, "welkom_refresh");

        if (refreshToken != null && !jwtUtils.isTokenExpire(refreshToken)) {
            String username = jwtUtils.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtils.validateToken(refreshToken, userDetails.getUsername())) {
                String newAccessToken = jwtUtils.generateAccessToken(userDetails.getUsername(), "ROLE_USER", null);
                ResponseCookie newAccessCookie = jwtUtils.generateCookie("welkom_access", newAccessToken, jwtExpirationTime);
                response.addHeader(HttpHeaders.SET_COOKIE, newAccessCookie.toString());
                return ResponseEntity.ok(Map.of("message", "Token mis à jour avec succès."));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Jeton de rafraîchissement invalide ou expiré."));
    }

    // 4. DÉCONNEXION
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cleanAccess = jwtUtils.getCleanCookie("welkom_access");
        ResponseCookie cleanRefresh = jwtUtils.getCleanCookie("welkom_refresh");
        response.addHeader(HttpHeaders.SET_COOKIE, cleanAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cleanRefresh.toString());
        return ResponseEntity.ok("Déconnexion réussie.");
    }
}

//import com.nathdev.welkom.dto.LoginRequest;
//import com.nathdev.welkom.dto.RegisterRequest;
//import com.nathdev.welkom.models.User;
//import com.nathdev.welkom.repositories.UserRepository;
//import com.nathdev.welkom.security.JwtUtils;
//import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.coyote.Response;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseCookie;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/v1/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthenticationManager authenticationManager;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtUtils jwtUtils;
//    private final UserDetailsService userDetailsService;
//    private final UserRepository userRepository;
//
//    @Value("${jwt.expire}")
//    private long jwtExpirationTime;
//
//    @Value("${jwt.refreshExpire}")
//    private long jwtRefreshExpire;
//
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest  registerRequest) {
//        try {
//            if (userRepository.findByUsername(registerRequest.username()).isPresent()) {
//                return ResponseEntity.badRequest().body("Ce nom d'utilisateur est déjà utilisé.");
//            }
//
//            if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
//                return ResponseEntity.badRequest().body("Cet e-mail est déjà utilisé.");
//            }
//
//            User user = new User();
//            user.setUsername(registerRequest.username());
//            user.setEmail(registerRequest.email());
//            user.setRole("USER");
//
//            user.setPassword(passwordEncoder.encode(registerRequest.password()));
//
//            userRepository.save(user);
//            return ResponseEntity.status(HttpStatus.CREATED).body(user);
//        }
//        catch (Exception e) {
//            log.error(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
//        try {
//            // On passe désormais l'email au gestionnaire d'authentification
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
//            );
//
//            if (authentication.isAuthenticated()) {
//                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//                // On récupère l'utilisateur par son e-mail (qui est l'identifiant principal dans userDetails)
//                User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
//
//                // Génération des jetons d'accès et de rafraîchissement
//                String accessToken = jwtUtils.generateAccessToken(user.getUsername(), "ROLE_" + user.getRole(), user.getEmail());
//                String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());
//
//                // Injection des cookies
//                ResponseCookie accessCookie = jwtUtils.generateCookie("welkom_access", accessToken, jwtExpirationTime);
//                ResponseCookie refreshCookie = jwtUtils.generateCookie("welkom_refresh", refreshToken, jwtRefreshExpire);
//
//                response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
//                response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
//
//                Map<String, Object> authData = new HashMap<>();
//                authData.put("username", userDetails.getUsername());
//                authData.put("role", user.getRole());
//
//                return ResponseEntity.ok(authData);
//            }
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Mot de passe ou email erroné"));
//        }
//        catch (AuthenticationException e) {
//                log.error("Erreur de connexion : {}", e.getMessage());
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
//        }
//    }
//
//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
//        String refreshToken = jwtUtils.getJwtFromCookies(request, "welkom_refresh");
//
//        if (refreshToken != null && !jwtUtils.isTokenExpire(refreshToken)) {
//            String username = jwtUtils.extractUsername(refreshToken);
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            if (jwtUtils.validateToken(refreshToken, userDetails.getUsername())) {
//                String newAccessToken = jwtUtils.generateAccessToken(userDetails.getUsername(), "ROLE_USER", null);
//                ResponseCookie newAccessCookie = jwtUtils.generateCookie("welkom_access", newAccessToken, jwtExpirationTime);
//                response.addHeader(HttpHeaders.SET_COOKIE, newAccessCookie.toString());
//                return ResponseEntity.ok("Token mis à jour.");
//            }
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Jeton de rafraîchissement invalide ou expiré.");
//    }
//}
