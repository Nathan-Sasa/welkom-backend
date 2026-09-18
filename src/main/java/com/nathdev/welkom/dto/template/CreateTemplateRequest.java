package com.nathdev.welkom.dto.template;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public record CreateTemplateRequest(

        @Schema(
                description = "Nom de la catalogue",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "Golden wedding"
        )
        String name,

        @Schema(
                description = "Catégorie de la catalogue",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "Wedding / Gala / BirthDay"
        )
        String category,

        @Schema(
                description = "Image de la couverture",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String catalogueImgUrl,

        @Schema(
                description = "Couleur de primaire de la catalogue en hexadecimal",
                example = "ffeeff"
        )
        String colorPrimary,

        @Schema(
                description = "Couleur d'accentuation de la catalogue en hexadecimal",
                example = "ec88dacd"
        )
        String colorAccent,

        @Schema(
                description = "Police d'écriture du titre",
                example = "Fair play"
        )
        String fontTitle,

        @Schema(
                description = "Police d'écriture du text du corps",
                example = "poppins"
        )
        String fontBody,

        @Schema(
                description = "Image contenue de la catalogue, optionnel"
        )
        String image1,

        @Schema(
                description = "Image contenue 2 de la catalogue, optionnel"
        )
        String image2,

        @Schema(
                description = "Image contenue 3 de la catalogue, optionnel"
        )
        String image3,

        @Schema(
                description = "Activer le cadre de la catalogue, optional"
        )
        boolean hasCadre,

        @Schema(
                description = "Contenue de la catalogue"
        )
        Map<String, Object> defaultConfig
) {
}
