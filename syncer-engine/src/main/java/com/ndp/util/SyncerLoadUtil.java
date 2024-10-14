package com.ndp.util;

import com.ndp.entity.syncer.Business;
import com.ndp.entity.syncer.Task;
import com.ndp.service.BusinessService;
import com.ndp.service.TaskService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class SyncerLoadUtil {

    @Inject
    Logger logger;
    @Inject
    TaskService taskService;
    @Inject
    BusinessService businessService;

    public void onStart(@Observes StartupEvent ev) {
        logger.info("Inicio de LoadService");
        try {
            loadTaskCSV("Task.csv");
            loadBusinessCSV("Business.csv");
        } catch (Exception e) {
            logger.error("Error en el proceso de carga de CSV", e);
        }
    }

    @Transactional
public void loadBusinessCSV(String fileName) throws Exception {
    logger.info("Inicio de loadBusinessCSV");

    ClassLoader classLoader = getClass().getClassLoader();
    try (InputStream inputStream = classLoader.getResourceAsStream(fileName);
         BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8))) {

        String headerLine = reader.readLine();
        if (headerLine == null) {
            logger.warn("El archivo CSV está vacío.");
            return;
        }

        // Trim the column names to remove any leading or trailing whitespace
        String[] columnNames = Arrays.stream(headerLine.split(";"))
                                     .map(String::trim)
                                     .toArray(String[]::new);

        List<Map<String, String>> rows = reader.lines()
                .map(line -> line.split(";"))
                .map(values -> {
                    Map<String, String> rowMap = new HashMap<>();
                    for (int i = 0; i < columnNames.length; i++) {
                        rowMap.put(columnNames[i], values[i].trim());
                    }
                    return rowMap;
                })
                .toList();

        logger.info("Se cargaron " + rows.size() + " filas para Business.");

        for (Map<String, String> row : rows) {
            String company = row.get("company");
            String business = row.get("business");
            Business oBusiness = businessService.findByCompanyAndBusiness(company, business);

            if (oBusiness == null) {
                oBusiness = new Business();
            }
            mapRowToBusiness(row, oBusiness);
            businessService.saveOrUpdate(oBusiness);
        }

    } catch (Exception e) {
        logger.error("Error al procesar el archivo CSV de Business", e);
        throw e;
    }
}

    private void mapRowToBusiness(Map<String, String> row, Business business) {
        business.setCompany(row.get("company"));
        business.setBusiness(row.get("business"));
        business.setPath(row.get("path"));
        business.setUser(row.get("user"));
        business.setPass(row.get("pass"));
        business.setCompanyBD(row.get("companyBD"));
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
                Task task = taskService.findCode(code);

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