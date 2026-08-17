package com.nathdev.welkom.dto.template;

import java.util.Map;
import java.util.UUID;

public record TemplateResponse(
        UUID uuid,
        String name,
        String category,
        String catalogueImgUrl,
        String colorPrimary,
        String colorAccent,
        String fontTitle,
        String fontBody,
        String image1,
        String image2,
        String image3,
        boolean hasCadre,
        Map<String, Object> defaultConfig
) {
}
