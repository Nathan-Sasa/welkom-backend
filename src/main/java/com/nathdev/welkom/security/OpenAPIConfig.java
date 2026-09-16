package com.nathdev.welkom.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de gestion des événements")
                        .version("1.0.0") // Point corrigé ici
                        .description("Documentation de l'API Task Manager")
                        .contact(new Contact()
                                .name("Nathan Sasa")
                                .email("jonathanbikuta05@gmail.com")))
                // On déclare que toutes les requêtes demandent par défaut le JWT et le Cookie
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth")
                        .addList("cookieAuth"))
                .components(new Components() // Espace corrigé ici
                        // Configuration pour le Header Authorization Bearer JWT
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                        // Configuration pour le Cookie (si vous stockez le JWT dans un cookie nommé 'token')
                        .addSecuritySchemes("cookieAuth",
                                new SecurityScheme()
                                        .name("token") // Remplacez par le nom exact de votre cookie
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.COOKIE)));
    }

}
