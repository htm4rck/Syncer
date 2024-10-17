package com.ndp.util;

import jakarta.enterprise.context.ApplicationScoped;

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
}
