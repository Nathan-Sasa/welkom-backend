package com.nathdev.welkom.dto.template;

import java.util.Map;

public record UpdateTemplateRequest(
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
        Boolean hasCadre,
        Map<String, Object> defaultConfig
) {
}
