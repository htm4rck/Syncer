package com.ndp.executor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndp.entity.syncer.Business;
import com.ndp.entity.syncer.Task;
import com.ndp.service.BusinessService;
import com.ndp.service.TaskService;
import com.ndp.types.QueueDto;
import com.ndp.types.rest.Response;
import com.ndp.util.BusinessConfig;
import com.ndp.service.rest.NDPServices;
import com.ndp.service.rest.SAPServices;
import com.ndp.util.Formatters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import io.quarkus.runtime.StartupEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
    @Inject
    Formatters formatters;
    private static final Map<String, String> classNameMapping = new HashMap<>();

    static {
        classNameMapping.put("PaymentReceipt_POS_DTO", "com.ndp.mapper.ndp.PaymentReceipt_POS_DTO");
        classNameMapping.put("SaleOrderDetail_POS_DTO", "com.ndp.mapper.ndp.SaleOrderDetail_POS_DTO");
        // Add other mappings as needed
    }

    public void onStart(@Observes StartupEvent ev) {
        Business businessNDP = businessService.findByCompanyAndBusiness(company, "NDP");
        Business businessSAP = businessService.findByCompanyAndBusiness(company, "SAP.SL");
        logger.warn("businessNDP: " + businessNDP);
        logger.warn("businessSAP: " + businessSAP);
        ndpServices = new NDPServices(businessNDP);
        sapServices = new SAPServices(businessSAP);
        logger.info("SyncerExecutorService initialized");
        List<QueueDto> queueList = getQueue();
        logger.warn("Objetos en Cola: " + queueList.size());
        List<Task> taskList = taskService.getAllTasks(1, 200);
        logger.warn("Tareas en BD: " + taskList.size());

        taskList.forEach((task) -> {
            List<QueueDto> filteredQueue = queueList.stream()
                    .filter(queue -> queue.getObject()
                            .equals(task.getSourceCode()))
                    .toList();
            logger.warn(task.getSourceCode() + " - # Pendientes: " + filteredQueue.size());
            processQueueByTask(task, filteredQueue);
        });
    }

    public void processQueueByTask(Task task, List<QueueDto> filteredQueue) {
        logger.warn(formatters.getLog(
                task.getDestinationCode(),
                task.getSourceCode(),
                task.getName(),
                "Ejecucion de Cola por Tarea" + task.getCode()
        ));
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
        Object object = getObjectNDP(queue);
        Object convertedObject = convertObject(task, object);
        sendObjectSAP(task, convertedObject);
        logger.warn("Procesando objeto: " + queue.toString());
        logger.warn("Procesando tarea: " + task.name);

    }

    public void sendObjectSAP(Task task, Object object) {
        sapServices.sendPostRequest(task.destinationPath, object);
    }

    public Object convertObject(Task task, Object object) {
        try {
            String destinationClassName = "com.ndp.mapper.sap." + task.getDestinationCode();
            Class<?> destinationClass = Class.forName(destinationClassName);
            Constructor<?> constructor = destinationClass.getConstructor(object.getClass());
            Object destinationObject = constructor.newInstance(object);
            ObjectMapper objectMapper = new ObjectMapper();
            logger.warn("Destination object: " + objectMapper.writeValueAsString(destinationObject));
            return destinationObject;
        } catch (ClassNotFoundException e) {
            logger.error("Clase no encontrada: " + task.getDestinationCode(), e);
            return null;
        } catch (NoSuchMethodException e) {
            logger.error("Constructor no encontrado en la clase: " + task.getDestinationCode(), e);
            return null;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Error al instanciar la clase: " + task.getDestinationCode(), e);
            return null;
        } catch (JsonProcessingException e) {
            logger.error("Error al convertir el objeto a JSON", e);
            throw new RuntimeException(e);
        }
    }

    public Object getObjectNDP( QueueDto queue ){
        try {
            String className = classNameMapping.get(queue.getObject());
            logger.warn("Clase: " + className);
            if (className == null) {
                throw new ClassNotFoundException("No mapping found for class: " + queue.getObject());
            }
            Class<?> clazz = Class.forName(className);
            Response<?> response = ndpServices.ndpGet(queue.getPath(), clazz);
            if (response != null && response.getData() != null) {
                List<?> valueList = response.getData().getValue();
                if (valueList != null && !valueList.isEmpty()) {
                    logger.warn("Value0: " + valueList.get(0));
                    Object firstObject = clazz.cast(valueList.get(0));
                    logger.warn("First object: " + firstObject.toString());
                    return firstObject;
                } else {
                    logger.warn("Value list is empty");
                    return null;
                }
            } else {
                logger.warn("Response or data is null");
                return null;
            }
        } catch (ClassNotFoundException e) {
            logger.error("Clase no encontrada: " + queue.getObject(), e);
            return null;

        }
    }

    public List<QueueDto> getQueue() {
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