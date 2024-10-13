package com.ndp.util;

import com.ndp.entity.syncer.Task;
import com.ndp.repository.TaskRepository;
import com.ndp.service.TaskService;
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
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class SyncerLoadUtil {

    private static final Logger logger = Logger.getLogger(SyncerLoadUtil.class);

    @Inject
    TaskRepository taskRepository;
    @Inject
    TaskService taskService;

    public void onStart(@Observes StartupEvent ev) {
        logger.info("Inicio de LoadService");
        try {
            loadTaskCSV("Task.csv");
        } catch (Exception e) {
            logger.error("Error en el proceso de carga de CSV", e);
        }
    }

    @Transactional
    public void loadTaskCSV(String fileName) throws Exception {
        logger.info("Inicio de loadTaskCSV");

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
                    .map(values -> {
                        Map<String, String> rowMap = new HashMap<>();
                        for (int i = 0; i < columnNames.length; i++) {
                            rowMap.put(columnNames[i], values[i]);
                        }
                        return rowMap;
                    })
                    .collect(Collectors.toList());

            logger.info("Se cargaron " + rows.size() + " filas para Task.");

            for (Map<String, String> row : rows) {
                String code = row.get("code");
                Task task = taskRepository.findByCode(code);

                if (task == null) {
                    task = new Task();
                }
                mapRowToTask(row, task);
                taskService.saveOrUpdate(task);
            }

        } catch (Exception e) {
            logger.error("Error al procesar el archivo CSV de Task", e);
            throw e;
        }
    }

    private void mapRowToTask(Map<String, String> row, Task task) {
        task.setGroup(row.get("group"));
        task.setSourceService(row.get("sourceService"));
        task.setSourceCode(row.get("sourceCode"));
        task.setSourcePath(row.get("sourcePath"));
        task.setSourceName(row.get("sourceName"));
        task.setDestinationCode(row.get("destinationCode"));
        task.setDestinationPath(row.get("destinationPath"));
        task.setDestinationName(row.get("destinationName"));
        task.setCode(row.get("code"));
        task.setName(row.get("name"));
        task.setApp(row.get("app"));
        task.setData(row.get("data"));
        task.setOrderExecution(Integer.parseInt(row.get("orderExecution")));

        task.setStatusOperation(row.get("statusOperation"));
        task.setStatus(row.get("status"));
        task.setBulkAdd(row.get("bulkAdd"));
        task.setMigrationType(row.get("migrationType"));
    }

}