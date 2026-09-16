package com.nathdev.welkom.dto.customizedTemplate;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;
import java.util.UUID;

public record CustomizeTemplateRequest(
        @Schema(
                description = "Uuid du template",
                example = "de8e6ff0-b40e-45d4-b214-41c90f212c70"
        )
        UUID templateUuid,

        @Schema(
                description = "Name de la catalogue",
                example = "Golden Widden"
        )
        String name,

        String customImage1,
        String customImage2,
        String customImage3,

        boolean customHasCadre,
        String customCadreUrl,
        String customFontTitle,
        String customFontBody,
        String customColorPrimary,
        String customColorAccent,

        @Schema(
                description = "Le contenu dynamique de la catalogue"
        )
        Map<String, Object> contentData
) {
}
