package com.ndp.executor;

import com.ndp.entity.syncer.Business;
import com.ndp.entity.syncer.Task;
import com.ndp.service.BusinessService;
import com.ndp.service.TaskService;
import com.ndp.types.QueueDto;
import com.ndp.types.rest.Response;
import com.ndp.service.rest.NDPServices;
import com.ndp.service.rest.SAPServices;
import com.ndp.util.Formatters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import io.quarkus.runtime.StartupEvent;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.*;

@ApplicationScoped
public class SyncerExecutorService {

    @Inject
    Logger logger;

    @Inject
    SAPServices sapServices;

    @Inject
    TaskService taskService;

    @Inject
    BusinessService businessService;

    @Inject
    Formatters formatters;

    private NDPServices ndpServices;
    private static final int NUM_THREADS = 4;
    private static final String COMPANY = "AZALEIA";
    private static final int MAX_ATTEMPTS = 3;
    private static final int ATTEMPS_NODO = 4;
    private static final Map<String, String> classNameMapping = new HashMap<>();

    static {
        classNameMapping.put("PaymentReceipt_POS_DTO", "com.ndp.mapper.ndp.PaymentReceipt_POS_DTO");
        classNameMapping.put("SaleOrderDetail_POS_DTO", "com.ndp.mapper.ndp.SaleOrderDetail_POS_DTO");
    }

    /**
     * Método de inicio que inicializa los servicios y procesa las colas y tareas.
     */
    public void onStart(@Observes StartupEvent ev) {
        Business businessNDP = businessService.findByCompanyAndBusiness(COMPANY, "NDP");
        Business businessSAP = businessService.findByCompanyAndBusiness(COMPANY, "SAP.SL");

        ndpServices = new NDPServices(businessNDP);
        sapServices = new SAPServices(businessSAP);

        logger.info("SyncerExecutorService initialized");

        List<QueueDto> queueList = getQueue();
        if (queueList != null) {
            //TODO: ObjectType vendria a ser el listado de objetos con la tipificacion carga | descarga;
            //TODO: Task vendria a ser la relacion entre Objectype y empresa
            //TODO: Asegurar con un sort que las tareas se ejecuten en orden
            List<Task> taskList = taskService.getAllTasks(1, 200);
            processTasks(taskList, queueList);
        }
    }

    /**
     * Procesa todas las tareas disponibles y sus colas correspondientes.
     */
    private void processTasks(List<Task> taskList, List<QueueDto> queueList) {
        taskList.forEach(task -> {
            List<QueueDto> filteredQueue = filterQueueByTask(queueList, task);
            processQueueByTask(task, filteredQueue);
        });
    }

    /**
     * Filtra la lista de colas según el código fuente de la tarea.
     */
    private List<QueueDto> filterQueueByTask(List<QueueDto> queueList, Task task) {
        return queueList.stream()
                .filter(queue -> queue.getObject().equals(task.getSourceCode()))
                .toList();
    }

    /**
     * Ejecuta en paralelo el procesamiento de objetos por tarea y cola.
     */
    private void processQueueByTask(Task task, List<QueueDto> filteredQueue) {
        logger.warn(formatters.getLog(task.getDestinationCode(), task.getSourceCode(), task.getName(),
                "Ejecución de Cola por Tarea " + task.getCode()));

        ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);
        CompletionService<Void> completionService = new ExecutorCompletionService<>(threadPool);

        filteredQueue.forEach(nodo ->
                completionService.submit(() -> {
                    processObject(task, nodo);
                    return null;
                })
        );

        shutdownThreadPool(threadPool, completionService);
    }

    /**
     * Procesa un objeto específico y lo envía a SAP.
     */
    private void processObject(Task task, QueueDto nodo) {
        Object object = getObjectNDP(nodo);
        assert object != null;
        Object convertedObject = convertObject(task, object);
        Optional<String> response = sendObjectSAP(task, convertedObject);
        handleQueueResponse(nodo, response);
    }

    /**
     * Envia el objeto convertido a SAP y maneja la respuesta.
     */
    private Optional<String> sendObjectSAP(Task task, Object object) {
        return sapServices.sendPostRequest(task.getDestinationPath(), object);
    }

    /**
     * Convierte el objeto de NDP al formato requerido por SAP.
     */
    private Object convertObject(Task task, Object object) {
        try {
            String destinationClassName = "com.ndp.mapper.sap." + task.getDestinationCode();
            Class<?> destinationClass = Class.forName(destinationClassName);
            Constructor<?> constructor = destinationClass.getConstructor(object.getClass());
            return constructor.newInstance(object);
        } catch (ReflectiveOperationException e) {
            logger.error("Error al convertir objeto: " + task.getDestinationCode(), e);
            return null;
        }
    }

    /**
     * Obtiene un objeto desde NDP.
     */
    private Object getObjectNDP(QueueDto queue) {
        try {
            String className = classNameMapping.get(queue.getObject());
            if (className == null) throw new ClassNotFoundException("No mapping found for class: " + queue.getObject());

            Class<?> classObject = Class.forName(className);
            Response<?> response = ndpServices.ndpGet(queue.getPath(), classObject);
            return extractDataFromResponse(response, classObject);
        } catch (ClassNotFoundException e) {
            logger.error("Clase no encontrada: " + queue.getObject(), e);
            return null;
        }
    }

    /**
     * Extrae el primer elemento de la lista de datos de una respuesta.
     */
    private Object extractDataFromResponse(Response<?> response, Class<?> clazz) {
        if (response != null && response.getData() != null) {
            List<?> valueList = response.getData().getValue();
            if (valueList != null && !valueList.isEmpty()) {
                return clazz.cast(valueList.get(0));
            }
        }
        logger.warn("No se encontraron datos en la respuesta");
        return null;
    }

    /**
     * Maneja la respuesta del envío del objeto y actualiza el estado de la cola.
     */
    private void handleQueueResponse(QueueDto queue, Optional<String> response) {
        QueueDto patchQueue = new QueueDto();
        patchQueue.setUid(queue.getUid());
        patchQueue.setAttempts(response.isPresent() ? 0 : queue.getAttempts() + 1);
        if (response.isPresent()) {
            logger.warn("Response from SAP: " + response.get());
            ndpServices.ndpDeleted("queue-engine/queue/" + queue.getUid(), Object.class);
        }else if (queue.getAttempts() == MAX_ATTEMPTS-1 ) {
            patchQueue.setStatus("FAILED");
        }
        ndpServices.ndpPatch("queue-engine/queue/" + queue.getUid(), patchQueue, Object.class);
    }

    /**
     * Recupera la lista de colas desde el servicio NDP.
     */
    private List<QueueDto> getQueue() {
        try {
            String url = String.format("queue-engine/queue?size=200&page=1&maxAttempts=%d&company=%s", MAX_ATTEMPTS, COMPANY);
            Response<QueueDto> sequence = ndpServices.ndpGet(url, QueueDto.class);
            if (sequence != null) {
                logger.info("Sequence data retrieved successfully: " + sequence);
                return sequence.getData().getPagination().getList();
            }
        } catch (Exception e) {
            logger.error("Error al obtener las colas", e);
        }
        return Collections.emptyList();
    }

    /**
     * Apaga el pool de threads y espera la finalización de las tareas.
     */
    private void shutdownThreadPool(ExecutorService threadPool,
                                    CompletionService<Void> completionService) {
        threadPool.shutdown();
        try {
            for (int i = 0; i < ATTEMPS_NODO; i++) {
                Future<Void> future = completionService.take();
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.warn("Error durante el procesamiento de tareas", e);
            threadPool.shutdownNow();
        }
    }
}
