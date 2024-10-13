package com.ndp.executor;

import com.ndp.entity.queue.Queue;
import com.ndp.entity.syncer.Task;
import com.ndp.repository.TaskRepository;
import com.ndp.util.BusinessConfig;
import com.ndp.service.rest.NDPServices;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import io.quarkus.runtime.StartupEvent;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SyncerExecutorService {

    private static final Logger logger = Logger.getLogger(SyncerExecutorService.class);
    @Inject
    TaskRepository taskRepository;

    List<Queue> listSequence = new ArrayList<>();
    BusinessConfig businessConfig;
    @Inject
    NDPServices ndpServices;
    //@Inject
    //private final QueueService queueService;


    public void onStart(@Observes StartupEvent ev) {
        logger.info("SyncerExecutorService initialized");
        //List<Queue> sequences = getSequences();
        List<Task> taskList = taskRepository.listAll();
        /*if (sequences != null) {
            taskList.forEach((task) -> {
                List<Queue> filteredSequences = sequences.stream()
                        .filter(sequence -> sequence.getObjectName()
                                .equals(task.getSourceCode()))
                        .collect(Collectors.toList());
                processQueueByTask(filteredSequences);
            });
        }*/
    }

    public void processQueueByTask(List<Queue> filteredQueue) {
        filteredQueue.forEach((sequence) -> {
            getObject(sequence);
        });
    }

    /*public List<Queue> getSequences() {
        //TODO: Traer Objetos pendientes de la cola
        try {
            Root<ndp.types.Queue> sequence = queueService.getSequenceData();
            if (sequence != null) {
                logger.info("Sequence data retrieved successfully: " + sequence);
                //his.listSequence = sequence.getData();
                return this.listSequence;
            } else {
                logger.error("Failed to retrieve sequence data");
                return null;
            }
        } catch (Exception e) {
            logger.error("Exception occurred while retrieving sequence data", e);
            return null;
        }
    }*/

    public void getObject(Queue sequence) {
        Task task = taskRepository.findBySourceCode(sequence.getObject());
        String URL = getURL(sequence.getCompany(), task.getSourceService()) + "/" + sequence.getPath();
        logger.warn(ndpServices.ndpGet(URL, Queue.class));
    }

    public String getURL(String company, String sourceService) {
        return businessConfig.getProperty(company, sourceService + ".path");
    }
}