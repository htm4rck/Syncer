package com.ndp.controller;

import com.ndp.types.rest.Data;
import com.ndp.types.rest.Pagination;
import com.ndp.types.rest.ResponseDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

import com.ndp.service.QueueService;
import com.ndp.entity.queue.Queue;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/queue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QueueController {

    @Inject
    Logger logger;
    @Inject
    QueueService queueService;

    @GET
    public ResponseDTO<Queue> getAllQueues(@HeaderParam("company") String company,
                                           @QueryParam("maxAttempts") @DefaultValue("3") int maxAttempts,
                                           @QueryParam("page") @DefaultValue("1") int page,
                                           @QueryParam("size") @DefaultValue("20") int size
                                        ) {
        List<Queue> queues;
        if (company != null && !company.isEmpty()) {
            queues = queueService.listQueuesByCompanyAndAttempts(company, maxAttempts, page, size);
        } else {
            queues = queueService.listAllQueues(page, size);
        }
        Data<Queue> data = new Data<>(new Pagination<>(queues.size(), page, queues));
        return new ResponseDTO<>(true, data);
    }

    @GET
    @Path("/{id}")
    public Queue getQueue(@PathParam("id") Long id) {
        return queueService.findQueueById(id);
    }

    @POST
    public void createQueue(Queue queue) {
        queueService.createQueue(queue);
    }

    @POST
    @Path("/bulk")
    public void createQueues(List<Queue> queues) {
        queueService.createQueues(queues);
    }

    @PUT
    @Path("/{id}")
    public void updateQueue(@PathParam("id") Long id, Queue queue) {
        queue.setId_queue(id);
        queueService.updateQueue(queue);
    }

    @DELETE
    @Path("/{id}")
    public void deleteQueue(@PathParam("id") Long id) {
        queueService.deleteQueue(id);
    }

    @PATCH
    @Path("/{uid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patchQueue(@PathParam("uid") String uid, Queue patchData) {
        if (!uid.equals(patchData.getUid())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Los UID de la cabecera y JSON  de la Cola no coinciden").build();
        }
        Queue updatedQueue = queueService.patchQueue(patchData);
        if (updatedQueue == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("La Cola no existe: " + uid).build();
        }
        return Response.ok(updatedQueue).build();
    }

}
