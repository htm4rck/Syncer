package com.ndp.service;

import com.ndp.entity.syncer.Task;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import java.util.List;

@ApplicationScoped
public class TaskService {

    @Inject
    EntityManager entityManager;
    @Inject
    Logger logger;

    @Transactional
    public Task saveOrUpdate(Task task) {
        Task existingTask = findCode(task.getCode());
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
            entityManager.persist(task);
            return task;
        }
    }

    public Task findCode(String code) {
        try {
            return entityManager.createNamedQuery("Task.findCode", Task.class)
                    .setParameter("code", code)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Task findBySourceCode(String sourceCode) {
        try {
            return entityManager.createNamedQuery("Task.findBySourceCode", Task.class)
                    .setParameter("sourceCode", sourceCode)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Task> getAllTasks(int page, int size) {
        int firstResult = (page - 1) * size;
        return entityManager.createQuery("SELECT t FROM Task t", Task.class)
                .setFirstResult(firstResult)
                .setMaxResults(size)
                .getResultList();
    }

}