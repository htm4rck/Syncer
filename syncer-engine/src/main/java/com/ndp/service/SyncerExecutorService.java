// SyncerExecutorService.java
package com.ndp.service;

import com.ndp.service.rest.SequenceService;
import com.ndp.types.Root;
import com.ndp.types.Sequence;
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
    SequenceService sequenceService;
    List<Sequence> listSequence = new ArrayList<>();

    public void onStart(@Observes StartupEvent ev) {
        logger.info("SyncerExecutorService initialized");
        List<Sequence> sequences = getSequences();
        if (sequences != null) {
            sequences.forEach((key) -> {
                logger.info("Key: " + key.getObjectName());
            });
        }
    }

    public List<Sequence> getSequences() {
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
}