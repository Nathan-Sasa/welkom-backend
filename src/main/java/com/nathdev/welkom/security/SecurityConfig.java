package com.nathdev.welkom.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            CustomDetailsService customDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(customDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/api/v1/auth/login",
                                "/login/**",
                                "/oauth2/**",
                                "/api/v1/public/**",
                                "/api/v1/checking/access",
                                "/api/v1/checking/logout",
                                "/v1/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // ***************************************
                        // Templates permissions *****************
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/templates/create"
                        ).hasAuthority("ROLE_WLK_ADMIN")
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/templates/{uuid}"
                        ).hasAuthority("ROLE_WLK_ADMIN")
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/templates/{uuid}"
                        ).hasAuthority("ROLE_WLK_ADMIN")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/templates/**"
                        ).permitAll()
                        //****************************************
                        //Event permissions **********************
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/events/**"
                        ).hasAuthority("ROLE_WLK_USER")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/events/**"
                        ).hasAuthority("ROLE_WLK_USER")
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/events/**"
                        ).hasAuthority("ROLE_WLK_USER")
                        .requestMatchers(
                                HttpMethod.DELETE,
                                        "/api/v1/events/**"
                        ).hasAuthority("ROLE_WLK_USER")
                        //****************************************
                        // CustomizedTemplate permissions ********
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/events/{eventUuid}/customized-template"
                        ).hasAuthority("ROLE_WLK_USER")
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/events/*/guests"
                        ).hasAuthority("ROLE_WLK_USER")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/events/{eventUuid}/customized-template"
                        ).hasAuthority("ROLE_WLK_USER")
                        //****************************************
                        // Guest permissions ********
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/events/*/guests"
                        ).hasAuthority("ROLE_WLK_USER")
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/events/*/guests"
                        ).hasAuthority("ROLE_WLK_USER")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/events/*/guests"
                        ).hasAuthority("ROLE_WLK_USER")
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/events/*/guests"
                        ).hasAuthority("ROLE_WLK_USER")
                        //****************************************
                        // Invitation permissions ********
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/events/*/invitation"
                        ).hasAuthority("ROLE_WLK_USER")
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/events/*/invitations"
                        ).hasAuthority("ROLE_WLK_USER")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/events/*/invitation"
                        ).hasAuthority("ROLE_WLK_USER")

                        //****************************************
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .oauth2Login(oauth2 -> oauth2.successHandler(oAuth2SuccessHandler)) // déscativé momementanément
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:4000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // echange de cookie avec le front

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
