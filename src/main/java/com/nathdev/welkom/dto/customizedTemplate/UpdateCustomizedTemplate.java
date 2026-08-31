package com.nathdev.welkom.dto.customizedTemplate;

import java.util.Map;

public record UpdateCustomizedTemplate(
        String name,
        String customImage1,
        String customImage2,
        String customImage3,
        Boolean customHasCadre,
        String customCadreUrl,
        String customFontTitle,
        String customFontBody,
        String customColorPrimary,
        String customColorAccent,
        Map<String, Object> contentData
) {
}
