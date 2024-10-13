// QueueLoadUtil.java
package com.ndp.util;

import com.ndp.entity.queue.Queue;
import com.ndp.service.QueueService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.jboss.logging.Logger;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import io.quarkus.runtime.StartupEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class QueueLoadUtil {

    private static final Logger logger = Logger.getLogger(QueueLoadUtil.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Inject
    QueueService queueService;

    public void onStart(@Observes StartupEvent ev) {
        logger.info("Inicio de LoadService");
        try {
            loadQueueCSV("Queue.csv");
        } catch (Exception e) {
            logger.error("Error en el proceso de carga de CSV", e);
        }
    }

    @Transactional
    public void loadQueueCSV(String fileName) throws Exception {
        logger.info("Inicio de loadQueueCSV");

        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();
            if (headerLine == null) {
                logger.warn("El archivo CSV está vacío.");
                return;
            }

            String[] columnNames = headerLine.split(";");

            List<Map<String, String>> rows = reader.lines()
                    .map(line -> line.split(";"))
                    .filter(values -> values.length == columnNames.length) // Ensure the row has the correct number of columns
                    .map(values -> {
                        Map<String, String> rowMap = new HashMap<>();
                        for (int i = 0; i < columnNames.length; i++) {
                            rowMap.put(columnNames[i], values[i]);
                        }
                        return rowMap;
                    })
                    .collect(Collectors.toList());

            logger.info("Se cargaron " + rows.size() + " filas para Queue.");

            for (Map<String, String> row : rows) {
                String code = row.get("code");
                Queue queue = queueService.findCode(code);
                logger.warn("Queue111: " + queueService.findCode(code));
                if (queue == null) {
                    queue = new Queue();
                }
                mapRowToQueue(row, queue);
                queueService.saveOrUpdate(queue);
            }

        } catch (Exception e) {
            logger.error("Error al procesar el archivo CSV de Queue", e);
            throw e;
        }
    }

    private void mapRowToQueue(Map<String, String> row, Queue queue) {
        queue.setMethod(row.get("method"));
        queue.setUid(row.get("uid"));
        queue.setCode(row.get("code"));
        queue.setDocument(row.get("document"));
        queue.setObject(row.get("object"));
        queue.setPath(row.get("path"));
        queue.setStoreCode(row.get("storeCode"));
        queue.setCompany(row.get("company"));
        queue.setCreateDate(LocalDateTime.parse(row.get("createDate"), DATE_TIME_FORMATTER));
        queue.setStatus(row.get("status"));
        queue.setAttempts(row.get("attempts") != null ? Integer.parseInt(row.get("attempts")) : null);
    }
}