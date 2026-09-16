package com.nathdev.welkom.dto.template;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public record UpdateTemplateRequest(

        @Schema(
                description = "modifier le nom de la catalogue"
        )
        String name,

        @Schema(
                description = "modifier la catégorie de la catalogue"
        )
        String category,

        @Schema(
                description = "modifier l'image de la couverture de la catalogue"
        )
        String catalogueImgUrl,

        @Schema(
                description = "Modifier la couleur de primaire de la catalogue en hexadecimal"
        )
        String colorPrimary,

        @Schema(
                description = "Modifier couleur d'accentuation de la catalogue en hexadecimal"
        )
        String colorAccent,

        @Schema(
                description = "Modifier la police d'écriture du titre"
        )
        String fontTitle,

        @Schema(
                description = "Modifier la police d'écriture du corps"
        )
        String fontBody,

        @Schema(
                description = "Modifier l'image contenue de la catalogue, optionale"
        )
        String image1,

        @Schema(
                description = "Modifier l'image contenue 2 de la catalogue, optionale"
        )
        String image2,

        @Schema(
                description = "Modififier Image contenue 3 de la catalogue, optionale"
        )
        String image3,

        @Schema(
                description = "Modifier l'activiation le cadre de la catalogue, optionale"
        )
        Boolean hasCadre,

        @Schema(
                description = "Modifier le contenu de la catalogue"
        )
        Map<String, Object> defaultConfig
) {
}
