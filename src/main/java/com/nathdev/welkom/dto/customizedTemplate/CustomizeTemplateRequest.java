package com.nathdev.welkom.dto.customizedTemplate;

import java.util.Map;
import java.util.UUID;

public record CustomizeTemplateRequest(
        UUID templateUuid,
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
        Map<String, Object> contentData
) {
}
