package com.ndp.service;

import com.ndp.entity.syncer.Task;
import com.ndp.repository.TaskRepository;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;

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

    public List<Task> getAllTasks() {
        return taskRepository.listAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findByIdOptional(id);
    }

    @Transactional
    public boolean deleteTask(Long id) {
        return taskRepository.deleteById(id);
    }
}