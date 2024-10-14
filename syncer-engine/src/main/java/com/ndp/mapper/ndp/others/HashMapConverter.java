package com.ndp.mapper.utils.dto.ndp.others;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Map;

public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {
    private static final Logger logger = LoggerFactory.getLogger(HashMapConverter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        String dataJson = null;
        try {
            dataJson  = objectMapper.writeValueAsString(attribute);
        } catch (final JsonProcessingException e) {
            logger.error("JSON writing error", e);
        }

        return dataJson;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        Map<String, Object> data = null;
        try {
            data = objectMapper.readValue(dbData, Map.class);
        } catch (final IOException e) {
            logger.error("JSON reading error", e);
        }

        return data;
    }

}

