package com.ndp.executor;

import com.ndp.entity.queue.Queue;
import com.ndp.entity.syncer.Business;
import com.ndp.entity.syncer.Task;
import com.ndp.service.BusinessService;
import com.ndp.service.TaskService;
import com.ndp.types.QueueDto;
import com.ndp.types.rest.Response;
import com.ndp.util.BusinessConfig;
import com.ndp.service.rest.NDPServices;
import com.ndp.service.rest.SAPServices;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import io.quarkus.runtime.StartupEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@ApplicationScoped
public class SyncerExecutorService {

    @Inject
    Logger logger;
    List<QueueDto> listSequence = new ArrayList<>();
    BusinessConfig businessConfig;
    String company = "AZALEIA";
    NDPServices ndpServices;
    @Inject
    SAPServices sapServices;
    int NUM_THREADS = 4;
    @Inject
    TaskService taskService;
    @Inject
    BusinessService businessService;

    private static final Map<String, String> classNameMapping = new HashMap<>();

    static {
        classNameMapping.put("PaymentReceipt_POS_DTO", "com.ndp.mapper.ndp.PaymentReceipt_POS_DTO");
        classNameMapping.put("SaleOrderDetail_POS_DTO", "com.ndp.mapper.ndp.SaleOrderDetail_POS_DTO");
        // Add other mappings as needed
    }

    public void onStart(@Observes StartupEvent ev) {
        Business businessNDP = businessService.findByCompanyAndBusiness(company, "NDP");
        Business businessSAP = businessService.findByCompanyAndBusiness(company, "SAP");
        logger.warn("businessNDP: " + businessNDP);
        ndpServices = new NDPServices(businessNDP);
        logger.info("SyncerExecutorService initialized");
        List<QueueDto> queueList = getSequences();
        logger.warn("Objetos en Cola: " + queueList.size());
        List<Task> taskList = taskService.getAllTasks(1, 200);
        logger.warn("Tareas en BD: " + taskList.size());

        taskList.forEach((task) -> {
            List<QueueDto> filteredSequences = queueList.stream()
                    .filter(sequence -> sequence.getObject()
                            .equals(task.getSourceCode()))
                    .toList();
            logger.warn(task.getSourceCode() + " - # Pendientes: " + filteredSequences.size());
            processQueueByTask(task, filteredSequences);
        });

    }

    public void processQueueByTask(Task task, List<QueueDto> filteredQueue) {

        ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);
        CompletionService<Void> completionService = new ExecutorCompletionService<>(threadPool);
        for (QueueDto queue : filteredQueue) {
            completionService.submit(() -> {
                processObject(task, queue);
                return null;
            });
        }
        threadPool.shutdown();
        try {
            for (int i = 0; i < filteredQueue.size(); i++) {
                Future<Void> future = completionService.take();
                try {
                    future.get();
                } catch (ExecutionException e) {
                    logger.warn("Error en el procesamiento de una operación", e.getCause());
                }
            }
        } catch (InterruptedException e) {
            logger.warn("Interrupción durante la espera de tareas", e);
            threadPool.shutdownNow();
        }

    }

    public void processObject(Task task, QueueDto queue) {
        try {
            // Obtén el nombre completo de la clase a partir del nombre simple
            String className = classNameMapping.get(queue.getObject());
            logger.warn("Clase: " + className);
            if (className == null) {
                throw new ClassNotFoundException("No mapping found for class: " + queue.getObject());
            }
            Class<?> clazz = Class.forName(className);
            // Llama al método ndpGet con la clase obtenida
            Response<?> response = ndpServices.ndpGet(queue.getPath(), clazz);

            // Extrae el primer objeto del value y asegúrate de que sea del tipo clazz
            if (response != null && response.getData() != null) {
                List<?> valueList = response.getData().getValue();
                if (valueList != null && !valueList.isEmpty()) {
                    logger.warn("Value0: " + valueList.get(0));
                    Object firstObject = clazz.cast(valueList.get(0));
                    logger.warn("First object: " + firstObject.toString());
                    // Trabaja con el objeto casteado
                } else {
                    logger.warn("Value list is empty");
                }
            } else {
                logger.warn("Response or data is null");
            }
        } catch (ClassNotFoundException e) {
            logger.error("Clase no encontrada: " + queue.getObject(), e);
        }
        logger.warn("Procesando objeto: " + queue.toString());
        logger.warn("Procesando tarea: " + task.toString());
    }


    public List<QueueDto> getSequences() {
        try {
            Response<QueueDto> sequence = ndpServices.ndpGet("queue-engine/queue?size=200&page=1", QueueDto.class);
            if (sequence != null) {
                logger.info("Sequence data retrieved successfully: " + sequence);
                this.listSequence = sequence.getData().getPagination().getList();
                return this.listSequence;
            } else {
                logger.error("Failed to retrieve sequence data");
                return null;
            }
        } catch (Exception e) {
            logger.error("Exception occurred while retrieving sequence data", e);
            return null;
        }
    }

}