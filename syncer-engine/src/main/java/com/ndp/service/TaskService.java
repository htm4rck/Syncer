package com.ndp.service;

import com.ndp.entity.Task;
import com.ndp.repository.TaskRepository;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.io.InputStream;

@ApplicationScoped
public class TaskService {

    private static final Logger logger = Logger.getLogger(TaskService.class);

    @Inject
    @Startup
    TaskRepository taskRepository;


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