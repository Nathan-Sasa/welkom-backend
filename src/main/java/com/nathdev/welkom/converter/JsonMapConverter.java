package com.nathdev.welkom.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.Map;

@Converter
public class JsonMapConverter implements AttributeConverter<Map<String, Object>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        if (attribute == null) {
            return null;
        }

        return objectMapper.writeValueAsString(attribute);
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String json) {
        if (json == null) {
            return null;
        }

        return objectMapper.readValue(
                json,
                new TypeReference<Map<String, Object>>() {}
        );
    }

}
