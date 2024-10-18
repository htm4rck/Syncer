package com.ndp.service;

import com.ndp.entity.queue.Queue;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class QueueService {

    @Inject
    EntityManager entityManager;
    @Inject
    Logger logger;

    @PostConstruct
    public void init() {
        if (entityManager == null) {
            logger.error("EntityManager was not injected!");
        } else {
            logger.info("EntityManager successfully injected.");
        }
    }

    @Transactional
    public void createQueue(Queue queue) {
        entityManager.persist(queue);
    }

    @Transactional
    public void createQueues(List<Queue> queues) {
        for (Queue queue : queues) {
            entityManager.persist(queue);
        }
    }

    @Transactional
    public Queue saveOrUpdate(Queue queue) {
        Queue existingQueue = findCode(queue.getCode());
        logger.warn("Queue: " + queue.getCode());
        logger.warn("QueueBody: " + queue.toString());
        if (existingQueue != null) {
            logger.warn("Queue already exists: " + queue.getCode());
            existingQueue.setMethod(queue.getMethod());
            existingQueue.setUid(queue.getUid());
            existingQueue.setCode(queue.getCode());
            existingQueue.setDocument(queue.getDocument());
            existingQueue.setObject(queue.getObject());
            existingQueue.setPath(queue.getPath());
            existingQueue.setStoreCode(queue.getStoreCode());
            existingQueue.setCompany(queue.getCompany());
            existingQueue.setCreateDate(queue.getCreateDate());
            existingQueue.setStatus(queue.getStatus());
            existingQueue.setAttempts(queue.getAttempts());
            return existingQueue;
        } else {
            entityManager.persist(queue);
            return queue;
        }
    }

    public Queue findQueueById(Long id) {
        return entityManager.find(Queue.class, id);
    }

    @Transactional
    public void updateQueue(Queue queue) {
        entityManager.merge(queue);
    }

    @Transactional
    public void deleteQueue(Long id) {
        Queue queue = entityManager.find(Queue.class, id);
        if (queue != null) {
            entityManager.remove(queue);
        }
    }

    public Queue findCode(String code) {
        try {
            return entityManager.createNamedQuery("Queue.findCode", Queue.class)
                    .setParameter("code", code)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Queue findUID(String uid) {
        try {
            return entityManager.createNamedQuery("Queue.findUID", Queue.class)
                    .setParameter("uid", uid)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public List<Queue> listQueuesByCompanyAndAttempts(String company, int maxAttempts, int page, int size) {
        int firstResult = (page - 1) * size;
        return entityManager.createQuery("SELECT q FROM Queue q WHERE q.company = :company AND (q.attempts < :maxAttempts OR q.attempts IS NULL)", Queue.class)
                .setParameter("company", company)
                .setParameter("maxAttempts", maxAttempts)
                .setFirstResult(firstResult)
                .setMaxResults(size)
                .getResultList();
    }

    public List<Queue> listAllQueues(int page, int size) {
        int firstResult = (page - 1) * size;
        return entityManager.createQuery("SELECT q FROM Queue q", Queue.class)
                .setFirstResult(firstResult)
                .setMaxResults(size)
                .getResultList();
    }

    @Transactional
    public Queue patchQueue(Queue queue) {
        try {
            Queue existingQueue = entityManager.createNamedQuery("Queue.findUID", Queue.class)
                    .setParameter("uid", queue.getUid())
                    .getSingleResult();
            if (existingQueue == null) {
                logger.warn("Queue not found: " + queue.getUid());
                return null;
            } else {
                mergeObjects(queue, existingQueue);
                entityManager.merge(existingQueue);
                return existingQueue;
            }
        } catch (Exception e) {
            logger.error("Error al Actualizar la Cola", e);
            return null;
        }
    }

    private void mergeObjects(Queue source, Queue target) {
        try {
            for (java.lang.reflect.Field field : source.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(source);
                if (value != null) {
                    field.set(target, value);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error while merging Queue objects", e);
        }
    }
}