package com.ndp.service;

import com.ndp.entity.Task;
import com.ndp.repository.TaskRepository;
import com.ndp.service.rest.SequenceService;
import com.ndp.types.Root;
import com.ndp.types.Sequence;
import com.ndp.util.BusinessConfig;
import com.ndp.util.NDPServices;
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
    @Inject
    SequenceService sequenceService;
    List<Sequence> listSequence = new ArrayList<>();
    BusinessConfig businessConfig;
    @Inject
    NDPServices ndpServices;

    public SyncerExecutorService() {
        this.businessConfig = new BusinessConfig(); // Initialize BusinessConfig
    }

    public void onStart(@Observes StartupEvent ev) {
        logger.info("SyncerExecutorService initialized");
        List<Sequence> sequences = getSequences();
        if (sequences != null) {
            sequences.forEach((sequence) -> {
                getObject(sequence);
                logger.info("Key: " + sequence.getObjectName());
            });
        }
    }

    public List<Sequence> getSequences() {
        //TODO: Traer Objetos pendientes de la cola
        try {
            Root<Sequence> sequence = sequenceService.getSequenceData();
            if (sequence != null) {
                logger.info("Sequence data retrieved successfully: " + sequence);
                this.listSequence = sequence.getData();
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

    public void getObject(Sequence sequence) {
        Task task = taskRepository.findBySourceCode(sequence.getObjectName());
        String URL = getURL(sequence.getCompanyId(), task.getSourceService())+sequence.getChannelOrigin().toLowerCase()+"/"+sequence.getPath();
        logger.warn("URL: " + URL);
        logger.warn(ndpServices.getObject(URL));
    }

    public String getURL(String company, String sourceService) {
        logger.warn(company + ".path");

        return businessConfig.getProperty(company, sourceService + ".path");
    }
}