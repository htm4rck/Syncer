package com.ndp.service;

import com.ndp.entity.Task;
import com.ndp.repository.TaskRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepository taskRepository;

    // Cargar datos desde Excel
    @Transactional
    public void loadTasksFromExcel(String filePath) throws Exception {
        try (InputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);  // Supone que los datos están en la primera hoja
            int numberOfRows = sheet.getPhysicalNumberOfRows();

            for (int i = 1; i < numberOfRows; i++) {
                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;  // Saltar filas vacías
                }

                String code = getCellValueAsString(row.getCell(8));  // Columna tx_code
                Task task = taskRepository.findByCode(code);

                if (task == null) {
                    task = new Task();
                }

                task.setGroup(getCellValueAsString(row.getCell(1)));
                task.setSourceCode(getCellValueAsString(row.getCell(2)));
                task.setSourcePath(getCellValueAsString(row.getCell(3)));
                task.setSourceName(getCellValueAsString(row.getCell(4)));
                task.setDestinationCode(getCellValueAsString(row.getCell(5)));
                task.setDestinationPath(getCellValueAsString(row.getCell(6)));
                task.setDestinationName(getCellValueAsString(row.getCell(7)));
                task.setCode(getCellValueAsString(row.getCell(8)));
                task.setName(getCellValueAsString(row.getCell(9)));
                task.setApp(getCellValueAsString(row.getCell(10)));
                task.setData(getCellValueAsString(row.getCell(11)));
                task.setOrderExecution(Integer.parseInt(getCellValueAsString(row.getCell(12))));
                task.setStatusOperation(getCellValueAsString(row.getCell(13)));
                task.setStatus(getCellValueAsString(row.getCell(14)));
                task.setBulkAdd(getCellValueAsString(row.getCell(15)));
                task.setMigrationType(getCellValueAsString(row.getCell(16)));

                taskRepository.persist(task);
            }
        }
    }

    // CRUD operations

    // Crear o actualizar una tarea
    @Transactional
    public Task saveOrUpdate(Task task) {
        Task existingTask = taskRepository.findByCode(task.getCode());
        if (existingTask != null) {
            existingTask.setName(task.getName());
            existingTask.setGroup(task.getGroup());
            existingTask.setSourceCode(task.getSourceCode());
            existingTask.setSourcePath(task.getSourcePath());
            existingTask.setSourceName(task.getSourceName());
            existingTask.setDestinationCode(task.getDestinationCode());
            existingTask.setDestinationPath(task.getDestinationPath());
            existingTask.setDestinationName(task.getDestinationName());
            existingTask.setApp(task.getApp());
            existingTask.setData(task.getData());
            existingTask.setOrderExecution(task.getOrderExecution());
            existingTask.setStatusOperation(task.getStatusOperation());
            existingTask.setStatus(task.getStatus());
            existingTask.setBulkAdd(task.getBulkAdd());
            existingTask.setMigrationType(task.getMigrationType());
            return existingTask;
        } else {
            taskRepository.persist(task);
            return task;
        }
    }

    // Obtener todas las tareas
    public List<Task> getAllTasks() {
        return taskRepository.listAll();
    }

    // Obtener una tarea por ID
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findByIdOptional(id);
    }

    // Eliminar una tarea por ID
    @Transactional
    public boolean deleteTask(Long id) {
        return taskRepository.deleteById(id);
    }

    // Método auxiliar para obtener el valor de una celda de Excel
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            default:
                return "";
        }
    }
}