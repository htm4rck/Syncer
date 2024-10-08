package com.ndp.service;

import com.ndp.entity.Task;
import com.ndp.repository.TaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.jboss.logging.Logger;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import io.quarkus.runtime.StartupEvent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class LoadService {

    private static final Logger logger = Logger.getLogger(LoadService.class);

    @PersistenceContext
    EntityManager entityManager;

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

    /**
     * Carga el archivo CSV de Task y guarda las filas en su repositorio correspondiente.
     *
     * @param fileName El nombre del archivo CSV.
     * @throws Exception Si ocurre algún error al procesar el archivo.
     */
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
                //logger.info("Task persistida: " + task.getCode());
            }

        } catch (Exception e) {
            logger.error("Error al procesar el archivo CSV de Task", e);
            throw e;
        }
    }

    /**
     * Mapea una fila del CSV a una instancia de Task.
     *
     * @param row Los datos de la fila representados como un mapa.
     * @param task La instancia de Task a actualizar.
     */
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

    /*
    @Transactional
    public void loadOtherEntityCSV(String fileName) throws Exception {

    }

    private OtherEntity mapRowToOtherEntity(Map<String, String> row) {
        OtherEntity entity = new OtherEntity();

        return entity;
    }
    */
}