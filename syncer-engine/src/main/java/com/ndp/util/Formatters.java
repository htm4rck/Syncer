package com.ndp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class Formatters {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public String getLog(String id, String Process, String Object, String message) {
        String threadName = Thread.currentThread().getName();
        String currentTime = LocalDateTime.now().format(formatter);
        return
                String.format(
                        "|Task|[(%s)]|%s|%s|%s|%s|%s|",
                        ZonedDateTime.now(ZoneId.of("America/Lima")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")),
                        threadName,
                        id ,
                        Process,
                        Object,
                        message

        );
    }

    public static String minifyJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        Object jsonObject = objectMapper.readValue(json, Object.class);
        return objectMapper.writeValueAsString(jsonObject);
    }
}
