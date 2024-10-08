package com.ndp.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public String getLog(String id, String Process, String Object, String message) {
        return
                String.format(
                        "|Task|[(%s)]|%s|%s|%s|%s|",
                        ZonedDateTime.now(ZoneId.of("America/Lima")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")),
                        id ,
                        Process,
                        Object,
                        message

        );
    }
}
