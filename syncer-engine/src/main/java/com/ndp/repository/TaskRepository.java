package com.ndp.repository;

import com.ndp.entity.Task;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskRepository implements PanacheRepository<Task> {

    public Task findByCode(String code) {
        return find("code", code).firstResult();
    }

    public Task findBySourceCode(String sourceCode) {
        return find("sourceCode", sourceCode).firstResult();
    }
}
